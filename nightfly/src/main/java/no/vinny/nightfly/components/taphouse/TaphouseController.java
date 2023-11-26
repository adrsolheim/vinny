package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.components.taphouse.domain.Tap;
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
    public List<Tap> allTaps() {
        return tapService.findAll();
    }

    @GetMapping("/{tap}")
    public Tap get(@PathVariable Long tap) {
        return tapService.find(tap).orElseThrow(() -> new ResourceNotFoundException("Tap not found " + tap));
    }

    @GetMapping("/active")
    public List<Tap> activeTaps() {
        return tapService.findActive();
    }

    @PostMapping("{tap}/connect/{batchId}")
    public Tap connectBatch(@PathVariable Long tap, @PathVariable Long batchId) {
        return tapService.connectBatch(tap, batchId);
    }
}
