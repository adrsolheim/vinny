package no.vinny.nightfly.components.common.sync;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SyncEntity(Long id,
                         @JsonProperty("_id") String brewfatherId,
                         @JsonProperty("_timestamp_ms") Long updated) { }
