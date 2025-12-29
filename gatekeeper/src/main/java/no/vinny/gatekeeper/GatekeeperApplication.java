package no.vinny.gatekeeper;

import lombok.extern.slf4j.Slf4j;
import no.vinny.gatekeeper.config.ServiceSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
public class GatekeeperApplication {
	@Autowired
	ServiceSettings serviceSettings;

	public static void main(String[] args) {
		SpringApplication.run(GatekeeperApplication.class, args);
	}

	@Bean
	ApplicationRunner clientRunner(RegisteredClientRepository repository, PasswordEncoder passwordEncoder) {
		return args -> {
			validateConfiguration();
			String clientId = "nightfly";
			RegisteredClient client = repository.findByClientId(clientId);
			log.info("::: service settings: {}", serviceSettings.getCredentials());
			log.info("::: settings: {}", serviceSettings.getSecret("nighfly"));
			if (client == null) {
				repository.save(
						RegisteredClient
      						.withId(UUID.randomUUID().toString())
      						.clientId(clientId)
      						.clientSecret(passwordEncoder.encode(serviceSettings.getSecret(clientId)))
      						.redirectUri("http://127.0.0.1:8082/login/oauth2/code/nightfly")
      						.redirectUri("http://127.0.0.1:8082/authorized")
      						.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      						.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
      						.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
      						.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
      						.scope(OidcScopes.OPENID)
      						.scope(OidcScopes.PROFILE)
      						.scope("api.nightfly")
							.tokenSettings(TokenSettings.builder()
									.accessTokenTimeToLive(Duration.ofHours(8))
									.refreshTokenTimeToLive(Duration.ofHours(10))
									.build())
      						.clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
      						.build()
				);
			}
			clientId = "sunflower";
			client = repository.findByClientId(clientId);
			log.info("::: client: {}", client);
			if (client == null) {
				repository.save(
						RegisteredClient
								.withId(UUID.randomUUID().toString())
								.clientId(clientId)
								.redirectUri("http://localhost:5000/login/oauth2/code/sunflower")
								.redirectUri("http://127.0.0.1:5000/login/oauth2/code/sunflower")
								//.redirectUri("http://127.0.0.1:5000/authorized")
								//.redirectUri("http://localhost:5000/callback/{registrationId}")
								.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
								.clientSettings(ClientSettings.builder()
										.requireAuthorizationConsent(false)
										.requireProofKey(true)
										.build())
								.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
								.scope("api.nightfly")
								.tokenSettings(TokenSettings.builder()
										.accessTokenTimeToLive(Duration.ofHours(8))
										.refreshTokenTimeToLive(Duration.ofHours(10))
										.build())
								.build()
				);
			}
			client = repository.findByClientId(clientId);
			log.info("{}", client.getRedirectUris());
		};
	}

	public void validateConfiguration() {
		if (serviceSettings == null) {
			throw new RuntimeException("ServiceSettings bean is null");
		}
		if (!StringUtils.hasText(serviceSettings.getSecret("nightfly")) || !StringUtils.hasText(serviceSettings.getSecret("sunflower"))) {
			throw new RuntimeException("Client secret is null or could not be injected. Ensure local.yml is present and populated");
		}
	}
}
