package no.vinny.nightfly.config;

import com.zaxxer.hikari.HikariDataSource;
import no.vinny.nightfly.batch.AsyncBatchRepository;
import no.vinny.nightfly.batch.AsyncBatchService;
import no.vinny.nightfly.batch.Mapper;
import no.vinny.nightfly.batch.impl.AsyncBatchServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public AsyncBatchService batchService(AsyncBatchRepository asyncBatchRepository) {
        return new AsyncBatchServiceImpl(asyncBatchRepository, new Mapper.ToDTO());
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
