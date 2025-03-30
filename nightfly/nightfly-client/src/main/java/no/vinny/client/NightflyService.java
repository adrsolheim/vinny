package no.vinny.client;

import no.vinny.nightfly.domain.Recipe;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Service
public class NightflyService {
    private final RestClient restClient;
    private static final String baseUrl = "http://localhost:8080";
    private static final String authUrl = "http://localhost:9000/oauth2/token";

    public NightflyService() {
        this.restClient = RestClient.create();
    }
    public Recipe getRecipe(Long id) {
        return restClient.get()
                .uri(baseUrl + "/api/recipes/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Recipe.class)
                .getBody();
    }

    public String auth() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        return restClient.post()
                .uri(authUrl)
                .body(body)
                .headers(headers -> {
                    headers.setBasicAuth("nightfly", "<injected secret>");
                })
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}
