package no.vinny.nightfly.batch;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class BatchObjectMapper {
    public static BatchDTO from(Batch batch) {
        return BatchDTO.builder()
                .brewfatherId(batch.getBrewfatherId())
                .name(batch.getName())
                .status(batch.getStatus().getValue())
                .build();
    }
    public static Batch from(BatchDTO dto) {
        return Batch.builder()
                .brewfatherId(dto.getBrewfatherId())
                .name(dto.getName())
                .status(Batch.Status.fromValue(dto.getStatus()))
                .build();
    }

    public static Batch from(Row row, RowMetadata rowMetadata) {
        return Batch.builder()
                .id(row.get("id", Long.class))
                .brewfatherId(row.get("brewfather_id", String.class))
                .name(row.get("name", String.class))
                .status(Batch.Status.valueOf(row.get("status", String.class)))
                .build();
    }
}
