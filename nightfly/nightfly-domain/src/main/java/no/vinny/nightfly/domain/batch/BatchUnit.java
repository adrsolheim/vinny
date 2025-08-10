package no.vinny.nightfly.domain.batch;

import lombok.Builder;
import lombok.Data;
import no.vinny.nightfly.domain.tap.TapStatus;

@Data
@Builder
public class BatchUnit {
    Long id;
    Long batchId;
    Long tapId;
    TapStatus tapStatus;
    Packaging packaging;
    VolumeStatus volumeStatus;
    Keg keg;
}
