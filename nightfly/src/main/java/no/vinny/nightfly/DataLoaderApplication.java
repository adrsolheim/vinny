package no.vinny.nightfly;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.AsyncBatchService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Profile;


@Profile("dev")
@Slf4j
public class DataLoaderApplication implements ApplicationRunner {

    private final AsyncBatchService asyncBatchService;

    public DataLoaderApplication(AsyncBatchService asyncBatchService) {
        this.asyncBatchService = asyncBatchService;
    }


    public static void main(String[] args) {
        log.info("STARTING : DataLoaderApplication run");
        SpringApplication.run(DataLoaderApplication.class, args);
    }

    // Not working
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("RUNNING : Inserting into database..");
        asyncBatchService.add(BatchDTO.builder()
               .brewfatherId("HY27A73dYWZNMxapgE4UdljPtNvDOL")
               .name("MarisOtter SMASH")
               .status(Batch.Status.COMPLETED.getValue())
               .build());
        asyncBatchService.add(BatchDTO.builder()
               .brewfatherId("LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ")
               .name("Eldon")
               .status(Batch.Status.COMPLETED.getValue())
               .build());
        asyncBatchService.add(BatchDTO.builder()
               .brewfatherId("NLltIcoo87foHbTz1N8rH0v0KXht6q")
               .name("Lutra")
               .status(Batch.Status.COMPLETED.getValue())
               .build());
        log.info("RUNNING : Run complete.");
    }
}
