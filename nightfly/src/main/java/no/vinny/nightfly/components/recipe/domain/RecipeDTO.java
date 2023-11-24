package no.vinny.nightfly.components.recipe.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RecipeDTO {
    private Long id;
    @NotEmpty
    private String brewfatherId;
    @NotEmpty
    private String name;
}
