package no.vinny.nightfly.components.taphouse;

import no.vinny.nightfly.components.taphouse.domain.Tap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/active")
    public List<Tap> activeTaps() {
        return tapService.findActive();
    }
}
