package no.vinny.nightfly.config;

import no.vinny.nightfly.service.BatchService;
import no.vinny.nightfly.service.impl.BatchServiceMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {


    @Bean
    public BatchService batchService() {
        return new BatchServiceMock();
    }
}
