package no.vinny.SimpleClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

	@GetMapping("/hello")
	public Map<String, String> root(Principal principal) {
		return Map.of("greeting", "Hello World!", "principal", principal.getName());
	}


	@GetMapping("/batches")
	public Map<String, String> batches(Principal principal) {
		return Map.of("b1", "Batch One", "principal", principal.getName());
	}

	@GetMapping("/recipes")
	public Map<String, String> recipes(Principal principal) {
		return Map.of("r1", "Recipe One", "principal", principal.getName());
	}
}
