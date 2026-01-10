package no.vinny.nightfly.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class Recipe {
    private Long id;
    @NotEmpty
    private String brewfatherId;
    @NotEmpty
    private String name;
    private ZonedDateTime updated;
}
