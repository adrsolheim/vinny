package no.vinny.nightfly.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private final String brewfatherId;
    @NotEmpty
    private final String name;
    // TODO: add enum validator
    private BatchStatus status;
}
