package no.vinny.nightfly.brewfather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import no.vinny.nightfly.batch.BatchStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class BatchJson {
    @JsonProperty("_id")
    private String brewfatherId;
    private Long batchNo;
    private Instant brewDate;
    private String brewer;
    private BatchJsonRecipe recipe;
    private BatchStatus status;

    @Data
    public static class BatchJsonRecipe {
        private String name;
    }
}
