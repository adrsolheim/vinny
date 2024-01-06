package no.vinny.nightfly.components.batch.domain;

import lombok.Builder;
import lombok.Data;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;

@Data
@Builder
public class BatchUnit {
    Long id;
    Long batchId;
    TapStatus tapStatus;
    Packaging packaging;
    VolumeStatus volumeStatus;
    Keg keg;
}
