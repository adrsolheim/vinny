package no.vinny.nightfly.config;

import no.vinny.nightfly.components.batch.BatchRepository;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Mapper;
import no.vinny.nightfly.components.batch.impl.BatchServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({Mapper.BatchJsonToDTO.class,
         Mapper.RecipeJsonToDTO.class})
@Configuration
public class AppConfig {


    @Bean
    public BatchService batchService(BatchRepository batchRepository) {
        return new BatchServiceImpl(batchRepository);
    }

}
