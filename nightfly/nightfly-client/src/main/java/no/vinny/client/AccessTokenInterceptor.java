package no.vinny.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;

public class AccessTokenInterceptor implements ClientHttpRequestInterceptor {

    private String token;
    private final String client;
    private final String secret;

    public AccessTokenInterceptor(String client, String secret) {
        this.client = client;
        this.secret = secret;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (request.getHeaders().get("Authorization") == null) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");
            Token token = RestClient.create().post()
                    .uri("http://localhost:9000/oauth2/token")
                    .body(params)
                    .headers(headers -> {
                        headers.setBasicAuth(client, secret);
                    })
                    .retrieve()
                    .toEntity(Token.class)
                    .getBody();
            request.getHeaders().set("Authorization", "Bearer " + token.access_token());
        }
        return execution.execute(request,body);
    }
}
