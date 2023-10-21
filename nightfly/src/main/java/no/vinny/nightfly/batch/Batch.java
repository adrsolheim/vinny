package no.vinny.nightfly.batch;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class Batch {
    private final Long id;
    private final String brewfatherId;
    private final String name;
    private final BatchStatus status;
    // TODO: filter batches for display on frontend
}
