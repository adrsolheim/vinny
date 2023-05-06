package no.vinny.nightfly.domain.util;

import no.vinny.nightfly.domain.data.Batch;
import no.vinny.nightfly.domain.dto.BatchDTO;

public class BatchMapper {
    public BatchDTO batchToDTO(Batch batch) {
        return BatchDTO.builder()
                .brewfatherId(batch.getBrewfatherId())
                .name(batch.getName())
                .status(batch.getStatus())
                .build();
    }
    public Batch dtoToBatch(BatchDTO dto) {
        return Batch.builder()
                .brewfatherId(dto.getBrewfatherId())
                .name(dto.getName())
                .status(dto.getStatus())
                .build();
    }
}
