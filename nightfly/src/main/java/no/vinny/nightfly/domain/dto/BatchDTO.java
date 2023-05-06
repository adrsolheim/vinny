package no.vinny.nightfly.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import no.vinny.nightfly.domain.data.BatchStatus;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BatchDTO {
    private final String brewfatherId;
    private final String name;
    private BatchStatus status;
}
