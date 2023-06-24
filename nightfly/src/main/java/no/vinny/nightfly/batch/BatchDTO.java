package no.vinny.nightfly.batch;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BatchDTO {
    @NotEmpty
    private final String brewfatherId;
    @NotEmpty
    private final String name;
    // TODO: add enum validator
    private Batch.Status status;
}
