package no.vinny.nightfly;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;


@Slf4j
public class DataLoaderApplication implements ApplicationRunner {

    private final BatchService batchService;

    public DataLoaderApplication(BatchService batchService) {
        this.batchService = batchService;
    }


    public static void main(String[] args) {
        log.info("STARTING : DataLoaderApplication run");
        SpringApplication.run(DataLoaderApplication.class, args);
    }

    // Not working
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("RUNNING : Inserting into database..");
        batchService.add(BatchDTO.builder()
               .brewfatherId("HY27A73dYWZNMxapgE4UdljPtNvDOL")
               .name("MarisOtter SMASH")
               .status(Batch.Status.COMPLETED)
               .build());
        batchService.add(BatchDTO.builder()
               .brewfatherId("LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ")
               .name("Eldon")
               .status(Batch.Status.COMPLETED)
               .build());
        batchService.add(BatchDTO.builder()
               .brewfatherId("NLltIcoo87foHbTz1N8rH0v0KXht6q")
               .name("Lutra")
               .status(Batch.Status.COMPLETED)
               .build());
        log.info("RUNNING : Run complete.");
    }
}
