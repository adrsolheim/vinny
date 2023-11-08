package no.vinny.nightfly.brewfather.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import no.vinny.nightfly.batch.domain.BatchStatus;

import java.time.Instant;

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
