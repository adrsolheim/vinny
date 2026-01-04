package no.vinny.nightfly.brewfather;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.brewfather.harvest.ScheduledImport;
import no.vinny.nightfly.components.batch.impl.BatchRepositoryImpl;
import no.vinny.nightfly.components.batch.impl.BatchServiceImpl;
import no.vinny.nightfly.components.recipe.impl.RecipeRepositoryImpl;
import no.vinny.nightfly.components.recipe.impl.RecipeServiceImpl;
import no.vinny.nightfly.config.redis.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@Import({
        BatchServiceImpl.class,
        BatchRepositoryImpl.class,
        RecipeServiceImpl.class,
        RecipeRepositoryImpl.class,
        RedisConfig.class})
@SpringBootApplication(scanBasePackages = {"no.vinny.nightfly.brewfather"})
@ConditionalOnProperty(name = "cron.runner.enabled", havingValue = "true")
@Slf4j
public class DataLoaderApplication implements ApplicationRunner {

    private ApplicationContext context;
    @Autowired
    private ScheduledImport scheduledImport;

    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(DataLoaderApplication.class);
        log.info("STARTING : DataLoaderApplication run");
        SpringApplication.run(DataLoaderApplication.class, args);
        log.info("RUNNING : Run complete.");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        scheduledImport.start();
    }
}