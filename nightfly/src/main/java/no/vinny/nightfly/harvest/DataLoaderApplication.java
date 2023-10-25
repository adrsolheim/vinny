package no.vinny.nightfly.harvest;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.impl.BatchRepositoryImpl;
import no.vinny.nightfly.batch.impl.BatchServiceImpl;
import no.vinny.nightfly.config.AppConfig;
import no.vinny.nightfly.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@Import({BatchServiceImpl.class, BatchRepositoryImpl.class, AppConfig.class})
@SpringBootApplication(scanBasePackages = {"no.vinny.nightfly.harvest"})
@ConditionalOnProperty(name = "cron.runner.enabled", havingValue = "true")
@Slf4j
public class DataLoaderApplication implements ApplicationRunner {

    private ApplicationContext context;
    @Autowired
    private ScheduledSync scheduledSync;

    public static void main(String[] args) {
        log.info("STARTING : DataLoaderApplication run");
        SpringApplication.run(DataLoaderApplication.class, args);
        log.info("RUNNING : Run complete.");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        scheduledSync.start();
    }
}