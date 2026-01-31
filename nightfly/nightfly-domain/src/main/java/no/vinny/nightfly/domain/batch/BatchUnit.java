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
    PackagingType packagingType;
    VolumeStatus volumeStatus;
    // is the keg currently filled with this batch unit?
    boolean occupiesKeg;
    Keg keg;
}
