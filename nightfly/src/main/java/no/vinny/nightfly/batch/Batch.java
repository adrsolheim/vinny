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
        FERMENTING("Fermenting", 0),
        BREWING("Brewing", 1),
        CONDITIONING("Conditioning", 2),
        COMPLETED("Completed", 3),
        SERVING("Serving", 4),
        ARCHIVED("Archived", 5);

        @Getter
        private final String value;
        private final int order;

        @Override
        public String toString() {
            return this.value;
        }
    }
}
