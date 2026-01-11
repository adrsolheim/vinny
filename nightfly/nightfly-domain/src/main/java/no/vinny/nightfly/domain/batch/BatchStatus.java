package no.vinny.nightfly.domain.batch;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

public enum BatchStatus {
    PLANNING("Planning", 0),
    FERMENTING("Fermenting", 1),
    BREWING("Brewing", 2),
    CONDITIONING("Conditioning", 3),
    COMPLETED("Completed", 4),
    SERVING("Serving", 5),
    ARCHIVED("Archived", 6);

    @Getter
    private final String value;
    @Getter
    private final int order;

    BatchStatus(String status, int order) {
        this.value = status;
        this.order = order;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BatchStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (BatchStatus s : BatchStatus.values()) {
            if (s.toString().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid BatchStatus");
    }
}
