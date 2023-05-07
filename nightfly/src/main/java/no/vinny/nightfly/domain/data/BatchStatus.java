package no.vinny.nightfly.domain.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BatchStatus {
    FERMENTING("Fermenting"),
    BREWING("Brewing"),
    CONDITIONING("Conditioning"),
    COMPLETED("Completed");

    @Getter
    private String value;

    @Override
    public String toString() {
        return this.value;
    }
}
