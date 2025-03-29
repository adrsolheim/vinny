package no.vinny.nightfly.components.batch.domain;

import lombok.Builder;
import lombok.Data;
import no.vinny.nightfly.components.taphouse.domain.TapStatus;

@Data
@Builder
public class BatchUnitDTO {

    private Long id;
    private Long batchId;
    private String brewfatherId;
    private String name;
    private TapStatus tapStatus;
    private Packaging packaging;
    private VolumeStatus volumeStatus;
    private Keg keg;
}
