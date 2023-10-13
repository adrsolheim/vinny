package no.vinny.nightfly;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.AsyncBatchService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;


@Slf4j
public class DataLoaderApplication implements ApplicationRunner {


    public static void main(String[] args) {
        log.info("STARTING : DataLoaderApplication run");
        SpringApplication.run(DataLoaderApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("RUNNING : Run complete.");
    }
}
