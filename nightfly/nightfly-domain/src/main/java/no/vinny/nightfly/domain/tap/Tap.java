package no.vinny.nightfly.domain.tap;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Tap {
    private Long id;
    private boolean active;
    private Long batchUnitId;
}
