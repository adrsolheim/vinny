package no.vinny.nightfly.components;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping
    public String publicResource() {
        return "This is public.";
    }

    @GetMapping("/protected")
    public String protectedResource() {
        return "This is protected.";
    }
}
