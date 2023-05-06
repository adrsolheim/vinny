package no.vinny.nightfly.domain.data;

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
    private final BatchStatus status;
}
