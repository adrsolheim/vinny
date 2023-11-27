package no.vinny.nightfly.config;

import com.zaxxer.hikari.HikariDataSource;
import no.vinny.nightfly.components.batch.AsyncBatchRepository;
import no.vinny.nightfly.components.batch.AsyncBatchService;
import no.vinny.nightfly.components.batch.BatchRepository;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Mapper;
import no.vinny.nightfly.components.batch.impl.AsyncBatchServiceImpl;
import no.vinny.nightfly.components.batch.impl.BatchServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Import({Mapper.BatchJsonToDTO.class,
         Mapper.RecipeJsonToDTO.class})
@Configuration
public class AppConfig {

    @Bean
    @Profile("reactive")
    public AsyncBatchService asyncBatchService(AsyncBatchRepository asyncBatchRepository) {
        return new AsyncBatchServiceImpl(asyncBatchRepository);
    }

    @Bean
    public BatchService batchService(BatchRepository batchRepository) {
        return new BatchServiceImpl(batchRepository);
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    public HikariDataSource hikariDataSource() {
        return DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(HikariDataSource hikariDataSource) {
        return new NamedParameterJdbcTemplate(hikariDataSource);
    }
}
