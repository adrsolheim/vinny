package no.vinny.SimpleClient;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Controller
@ResponseBody
@SpringBootApplication
public class SimpleClientApplication {


	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}

	@GetMapping("/jwt")
	public Map<String, String> jwt(RestTemplate restTemplate) {
		return Map.of("jwt", getJwtToken(restTemplate));
	}

	@GetMapping("/auth-jwt")
	public Map<String, String> jwt(Authentication authentication, RestTemplate restTemplate) {
		return Map.of("jwt", getJwtToken(restTemplate), "authentication", authentication.toString(), "authclass", authentication.getClass().getName());
	}


	private String getJwtToken(RestTemplate restTemplate) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth("nightfly", "578b7f8b-7e0b-452c-9a80-1541c6282ea3");
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "client_credentials");
		// space delimited
		body.add("scope", "batches.read batches.write recipes.read recipes.write taps.read taps.write openid profile");

		HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
		String url = "http://localhost:9000/oauth2/token";

		ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, entity, JsonNode.class);
		Assert.state(response.getStatusCode().is2xxSuccessful(), "the response needs to be 2xx");
		return response.getBody().get("access_token").asText();
	}

	@GetMapping("/")
	public Map<String, String> root(Authentication authentication) {
		return Map.of("greeting", "Hello World!", "authentication", authentication.toString(), "authclass", authentication.getClass().getName());
	}


	@GetMapping("/batches")
	public ResponseEntity<Map> batches(RestTemplate restTemplate) {
		String jwtToken = getJwtToken(restTemplate());

		LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(jwtToken);

		return restTemplate.exchange("http://localhost:8080/api/batches", HttpMethod.GET, new HttpEntity<>(body, headers), Map.class);
	}

	@PreAuthorize("isAuthenticated() and hasRole('admin')")
	@GetMapping("/recipes")
	public Map<String, String> recipes(Authentication authentication) {
		return Map.of("r1", "Recipe One", "authentication", authentication.toString(), "authclass", authentication.getClass().getName());
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
