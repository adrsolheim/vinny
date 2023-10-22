package no.vinny.nightfly.batch;

import org.springframework.stereotype.Service;

import java.util.function.Function;

public class Mapper {
    @Service
    public static class ToDTO implements Function<Batch, BatchDTO> {
        @Override
        public BatchDTO apply(Batch batch) {
            return BatchDTO.builder()
                    .id(batch.getId())
                    .brewfatherId(batch.getBrewfatherId())
                    .name(batch.getName())
                    .status(batch.getStatus() == null ? null : batch.getStatus().getValue())
                    .build();
        }
    }
    @Service
    public static class ToBatch implements Function<BatchDTO, Batch> {
        @Override
        public Batch apply(BatchDTO batchDTO) {
            return no.vinny.nightfly.batch.Batch.builder()
                    .id(batchDTO.getId())
                    .brewfatherId(batchDTO.getBrewfatherId())
                    .name(batchDTO.getName())
                    .status(BatchStatus.fromValue(batchDTO.getStatus()))
                    .build();
        }
    }
}
