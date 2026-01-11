package no.vinny.nightfly;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.common.sync.SyncService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Slf4j
@Order(2) // Run after import
@Component
@ConditionalOnProperty(name = "cron.runner.enabled", havingValue = "true")
public class SyncEntitiesApplication implements ApplicationRunner {

    private final SyncService syncService;

    public SyncEntitiesApplication(SyncService syncService) {
        this.syncService = syncService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        syncService.syncAll();
    }
}
