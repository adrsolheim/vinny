package no.vinny.nightfly.domain.batch;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum BatchStatus {
    FERMENTING("Fermenting", 0),
    BREWING("Brewing", 1),
    CONDITIONING("Conditioning", 2),
    COMPLETED("Completed", 3),
    SERVING("Serving", 4),
    ARCHIVED("Archived", 5);

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
        if (value == null) {
            return null;
        }
        for (BatchStatus s : BatchStatus.values()) {
            if (s.toString().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException();
    }
}
