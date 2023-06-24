package no.vinny.nightfly.batch;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Batch {
    private final Long id;
    private final String brewfatherId;
    private final String name;
    private final Status status;
    // TODO: filter batches for display on frontend

    @AllArgsConstructor
    public enum Status {
        FERMENTING("Fermenting"),
        BREWING("Brewing"),
        CONDITIONING("Conditioning"),
        COMPLETED("Completed");

        @Getter
        private final String value;

        @Override
        public String toString() {
            return this.value;
        }
    }
}
