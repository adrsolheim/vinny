package no.vinny.nightfly;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.AsyncBatchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;


@Slf4j
public class DataLoaderApplication implements ApplicationRunner {

    @Value("${brewfather.api_key}")
    private String brewfatherApiKey;
    @Value("${brewfather.user_id}")
    private String brewfatherUserId;

    public static void main(String[] args) {
        log.info("STARTING : DataLoaderApplication run");
        SpringApplication.run(DataLoaderApplication.class, args);
        log.info("CLOSING : DataLoaderApplication shutdown");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RestClient client = RestClient.create();
        String result = client.get()
                .uri("https://api.brewfather.app/v2/batches")
                .headers(h -> h.setBasicAuth(brewfatherUserId, brewfatherApiKey))
                .retrieve()
                .body(String.class);
        log.info("Response: {}", result);
        log.info("RUNNING : Run complete.");
    }
}
