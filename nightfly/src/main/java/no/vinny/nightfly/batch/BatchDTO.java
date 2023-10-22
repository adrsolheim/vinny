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
    private Long id;
    @NotEmpty
    private String brewfatherId;
    @NotEmpty
    private String name;
    // TODO: add enum validator
    private String status;
}
