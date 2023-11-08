package no.vinny.nightfly.batch.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import no.vinny.nightfly.recipe.domain.RecipeDTO;

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
    private Packaging packaging;
    private RecipeDTO recipe;
}
