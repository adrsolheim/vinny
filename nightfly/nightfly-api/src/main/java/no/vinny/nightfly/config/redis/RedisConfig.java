package no.vinny.nightfly.config.redis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import no.vinny.nightfly.domain.batch.Batch;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

@ConditionalOnProperty(prefix = "spring", name = "cache.type", havingValue = "redis")
@Configuration
@Import({RedisAutoConfiguration.class})
public class RedisConfig implements CachingConfigurer {
  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.port}")
  private int redisPort;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisCacheConfiguration cacheConfiguration) {
    ObjectMapper objectMapper = createObjectMapper();
    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(cacheConfiguration)
        .initialCacheNames(Set.of("batch", "batches"))
        .withInitialCacheConfigurations(Map.of(
                "batch",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(5L)).serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                //"batches",
                //RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(15L)).serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))
        ))
        .build();
  }

  @Bean
  @Override
  public CacheErrorHandler errorHandler() {
    return new RedisErrorHandler();
  }

  @Bean
  public RedisCacheConfiguration defaultCacheConfiguration() {
    ObjectMapper objectMapper = createObjectMapper();

    return RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(1L))
            .serializeValuesWith(SerializationPair.fromSerializer(new GZippingJackson2JsonRedisSerializer(objectMapper)));
  }

  private ObjectMapper createObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setConstructorDetector(ConstructorDetector.USE_PROPERTIES_BASED);
    objectMapper.enable(new JsonGenerator.Feature[]{JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN});
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, new SerializationFeature[]{SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, SerializationFeature.FAIL_ON_EMPTY_BEANS});
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, new DeserializationFeature[]{DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE});
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

    return objectMapper;
  }


  @Bean
  @Qualifier("batch-template")
  public RedisTemplate<String, Batch> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Batch> redisTemplate = new RedisTemplate<>();
    ObjectMapper objectMapper = createObjectMapper();
    GZippingJackson2JsonRedisSerializer redisSerializer = new GZippingJackson2JsonRedisSerializer(objectMapper);

    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(redisSerializer);
    redisTemplate.setHashValueSerializer(redisSerializer);
    redisTemplate.setEnableTransactionSupport(true);
    redisTemplate.afterPropertiesSet();

    return redisTemplate;
  }
}
