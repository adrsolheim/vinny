package no.vinny.client;

import no.vinny.nightfly.domain.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class NightflyService {
    private final String clientSecret;
    private static final String baseUrl = "http://localhost:8080";
    private static final String authUrl = "http://localhost:9000/oauth2/token";

    private final RestClient restClient;

    public NightflyService(@Value("${nightfly.secret}") String clientSecret) {
        this.restClient = RestClient.builder().requestInterceptor(new AccessTokenInterceptor("nightfly", clientSecret)).build();
        this.clientSecret = clientSecret;
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
                    headers.setBasicAuth("nightfly", clientSecret);
                })
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}
