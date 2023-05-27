package no.vinny.nightfly.batch;

public class BatchObjectMapper {
    public static BatchDTO from(Batch batch) {
        return BatchDTO.builder()
                .brewfatherId(batch.getBrewfatherId())
                .name(batch.getName())
                .status(batch.getStatus())
                .build();
    }
    public static Batch from(BatchDTO dto) {
        return Batch.builder()
                .brewfatherId(dto.getBrewfatherId())
                .name(dto.getName())
                .status(dto.getStatus())
                .build();
    }
}
