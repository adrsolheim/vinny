package no.vinny.SimpleClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Map;

@Controller
@ResponseBody
@SpringBootApplication
public class SimpleClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}


	@GetMapping("/jwt")
	public Map<String, String> jwt(Authentication authentication) {
		return Map.of("jwt", getJwtToken(), "authentication", authentication.toString(), "authclass", authentication.getClass().getName());
	}


	private String getJwtToken() {
		var restTemplate = new RestTemplate();
		var headers = new HttpHeaders();
		headers.setBasicAuth("simpleclient", "simpleclient");
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		var body = new LinkedMultiValueMap<String, String>();
		body.add("grant_type", "client_credentials");
		body.add("scope", "user.read");

		var entity = new HttpEntity<>(body, headers);
		var url = "http://localhost:8080/oauth2/token";
		var response = restTemplate.postForEntity(url, entity, JsonNode.class);
		Assert.state(response.getStatusCode().is2xxSuccessful(), "the response needs to be 2xx");
		return response.getBody().get("access_token").asText();
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
