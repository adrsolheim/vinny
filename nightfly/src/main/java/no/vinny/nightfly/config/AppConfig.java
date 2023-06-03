package no.vinny.nightfly.config;

import com.zaxxer.hikari.HikariDataSource;
import no.vinny.nightfly.batch.BatchRepository;
import no.vinny.nightfly.batch.impl.BatchRepositoryImpl;
import no.vinny.nightfly.batch.BatchService;
import no.vinny.nightfly.batch.impl.BatchServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class AppConfig {


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

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate() {
        return new R2dbcEntityTemplate();
    }

    @Bean
    public BatchRepository batchRepository(NamedParameterJdbcTemplate jdbcTemplate, R2dbcEntityTemplate r2dbcEntityTemplate) {
        return new BatchRepositoryImpl(jdbcTemplate, r2dbcEntityTemplate);
    }
}
