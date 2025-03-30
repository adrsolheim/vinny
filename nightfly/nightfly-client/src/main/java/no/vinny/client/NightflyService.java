package no.vinny.client;

import no.vinny.nightfly.domain.Recipe;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class NightflyService {
    private final RestClient restClient;
    private static final String baseUrl = "http://localhost:8080";

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
}
