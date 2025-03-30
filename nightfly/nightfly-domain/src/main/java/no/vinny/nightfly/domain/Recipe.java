package no.vinny.nightfly.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recipe {
    private Long id;
    @NotEmpty
    private String brewfatherId;
    @NotEmpty
    private String name;
}
