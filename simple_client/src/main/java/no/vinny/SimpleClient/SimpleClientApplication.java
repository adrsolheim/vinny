package no.vinny.SimpleClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@Controller
@ResponseBody
@SpringBootApplication
public class SimpleClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}

	@GetMapping("/")
	public Map<String, String> root(Authentication authentication) {
		return Map.of("greeting", "Hello World!", "authentication", authentication.toString(), "authclass", authentication.getClass().getName());
	}


	@PreAuthorize("isAuthenticated() and hasRole('user')")
	@GetMapping("/batches")
	public Map<String, String> batches(Authentication authentication) {
		return Map.of("b1", "Batch One", "authentication", authentication.toString(), "authclass", authentication.getClass().getName());
	}

	@PreAuthorize("isAuthenticated() and hasRole('admin')")
	@GetMapping("/recipes")
	public Map<String, String> recipes(Authentication authentication) {
		return Map.of("r1", "Recipe One", "authentication", authentication.toString(), "authclass", authentication.getClass().getName());
	}
}
