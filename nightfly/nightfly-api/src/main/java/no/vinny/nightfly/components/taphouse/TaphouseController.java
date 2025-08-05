package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.domain.tap.TapDTO;
import no.vinny.nightfly.util.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taphouse")
public class TaphouseController {

    private final TapService tapService;

    public TaphouseController(TapService tapService) {
        this.tapService = tapService;
    }

    @GetMapping
    public List<TapDTO> allTaps() {
        return tapService.findAll();
    }

    @GetMapping("/{tap}")
    public TapDTO get(@PathVariable Long tap) {
        return tapService.find(tap).orElseThrow(() -> new ResourceNotFoundException("Tap not found " + tap));
    }

    @GetMapping("/active")
    public List<TapDTO> activeTaps() {
        return tapService.findActive();
    }

    @PostMapping("{tap}/connect/{batchUnitId}")
    public TapDTO connectBatch(@PathVariable Long tap, @PathVariable Long batchUnitId) {
        return tapService.connectBatch(tap, batchUnitId);
    }
}
