package no.vinny.nightfly.domain.batch;

import lombok.Builder;
import lombok.Data;
import no.vinny.nightfly.domain.tap.TapStatus;

@Data
@Builder
public class BatchUnitDTO {

    private Long id;
    private Long batchId;
    private Long tapId;
    private String brewfatherId;
    private String name;
    private TapStatus tapStatus;
    private PackagingType packagingType;
    private VolumeStatus volumeStatus;
    private Keg keg;
}
