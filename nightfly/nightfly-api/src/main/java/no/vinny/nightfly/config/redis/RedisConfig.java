package no.vinny.nightfly.config.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.vinny.nightfly.domain.batch.Batch;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.ConstructorDetector;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

@ConditionalOnProperty(prefix = "spring", name = "cache.type", havingValue = "redis")
@Configuration
public class RedisConfig implements CachingConfigurer {
  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisCacheConfiguration cacheConfiguration) {
    ObjectMapper objectMapper = createObjectMapper();
    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(cacheConfiguration)
        .initialCacheNames(Set.of("batch", "batches"))
        .withInitialCacheConfigurations(Map.of(
                "batch",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(5L)).serializeValuesWith(SerializationPair.fromSerializer(new GenericJacksonJsonRedisSerializer(objectMapper)))
                //"batches",
                //RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(15L)).serializeValuesWith(SerializationPair.fromSerializer(new GenericJacksonJsonRedisSerializer(objectMapper))
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
    return JsonMapper.builder()
        .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED)
        .enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, DateTimeFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(DateTimeFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
        .changeDefaultPropertyInclusion(ignored -> JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
        .activateDefaultTyping(
            BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).allowIfSubType((context, subType) -> true).build(),
            DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        )
        .build();
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
