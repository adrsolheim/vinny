package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.components.taphouse.api.ConnectBatchRequest;
import no.vinny.nightfly.domain.tap.TapDTO;
import no.vinny.nightfly.components.common.error.NotFoundException;
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
        return tapService.find(tap).orElseThrow(() -> new NotFoundException("Tap not found " + tap));
    }

    @GetMapping("/active")
    public List<TapDTO> activeTaps() {
        return tapService.findActive();
    }

    @PostMapping("/{tap}/connect/{batchUnitId}")
    public TapDTO connectBatch(@PathVariable Long tap,
                               @PathVariable Long batchUnitId,
                               @RequestParam(required = false) Boolean oldBatchEmpty) {
        ConnectBatchRequest request = new ConnectBatchRequest(tap, batchUnitId, !Boolean.FALSE.equals(oldBatchEmpty));
        return tapService.connectBatch(request);
    }
}
