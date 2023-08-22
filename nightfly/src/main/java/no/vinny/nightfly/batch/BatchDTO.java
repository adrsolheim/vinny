package no.vinny.nightfly.batch;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Builder
public class BatchDTO {
    private final Long id;
    @NotEmpty
    private final String brewfatherId;
    @NotEmpty
    private final String name;
    // TODO: add enum validator
    private final String status;
}
