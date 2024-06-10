package no.vinny.gatekeeper;

import no.vinny.gatekeeper.config.NightflySettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.UUID;

@SpringBootApplication
public class GatekeeperApplication {
	@Autowired
	NightflySettings nightflySettings;

	public static void main(String[] args) {
		SpringApplication.run(GatekeeperApplication.class, args);
	}

	@Bean
	ApplicationRunner clientRunner(RegisteredClientRepository repository, PasswordEncoder passwordEncoder) {
		return args -> {
			String clientId = "nightfly";
			if (repository.findByClientId(clientId) == null) {
				repository.save(
						RegisteredClient
      						.withId(UUID.randomUUID().toString())
      						.clientId("nightfly")
      						.clientSecret(passwordEncoder.encode(nightflySettings.getSecret()))
      						.redirectUri("http://127.0.0.1:8080/login/oauth2/code/nightfly")
      						.redirectUri("http://127.0.0.1:8080/authorized")
      						.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      						.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
      						.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
      						.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      						.scope(OidcScopes.OPENID)
      						.scope(OidcScopes.PROFILE)
      						.scope("api.nightfly")
      						.clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
      						.build()
				);
			}
		};
	}
}
