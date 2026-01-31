package no.vinny.nightfly.components.keg;

import no.vinny.nightfly.domain.batch.Keg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/keg")
public class KegController {

    private final KegService kegService;

    public KegController(KegService kegService) {
        this.kegService = kegService;
    }

    @GetMapping
    public List<Keg> findAll(@RequestParam(defaultValue = "false") Boolean availableOnly) {
        return kegService.findAll(availableOnly);
    }
}
