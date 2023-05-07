package no.vinny.nightfly.domain.util;

import no.vinny.nightfly.domain.data.Batch;
import no.vinny.nightfly.domain.dto.BatchDTO;

public class BatchMapping {
    public static BatchDTO batchToDTO(Batch batch) {
        return BatchDTO.builder()
                .brewfatherId(batch.getBrewfatherId())
                .name(batch.getName())
                .status(batch.getStatus())
                .build();
    }
    public static Batch dtoToBatch(BatchDTO dto) {
        return Batch.builder()
                .brewfatherId(dto.getBrewfatherId())
                .name(dto.getName())
                .status(dto.getStatus())
                .build();
    }
}
