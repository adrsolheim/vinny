package no.vinny.gatekeeper.config;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import no.vinny.gatekeeper.event.CreateRsaKeyPairEvent;
import no.vinny.gatekeeper.keys.JdbcRsaKeyPairRepository;
import no.vinny.gatekeeper.keys.KeyGenerator;
import no.vinny.gatekeeper.keys.RsaKeyPairRepository;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // Enable OpenID Connect 1.0
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());
        // Accept and respond to protected resource requests using access tokens
        http.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));
        return http
                // Unauthenticated -> redirect to login
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login")))
                                //new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://gatekeeper:9000")
                .build();
    }

    //@Bean
    //public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    //    //byte[] seed = MessageDigest.getInstance("SHA-256").digest(nightflySettings.getSecret().getBytes("utf-8"));
    //    //RSAKey rsaKey = createRsaKey(seed);
    //    RSAKey rsaKey = loadRsaKeyPair();
    //    JWKSet jwkSet = new JWKSet(rsaKey);
    //    return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    //}

    private static RSAKey createRsaKey(byte[] seed) throws NoSuchAlgorithmException {
        KeyPair keyPair = createKeyPair(seed);
        RSAPublicKey publicKey   = (RSAPublicKey)  keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static RSAKey loadRsaKeyPair() {
        String pubText;
        String privText;
        try {
            pubText = Files.readString(Path.of(AuthorizationServerConfig.class.getClassLoader().getResource("publickey.pem").toURI()));
            privText = Files.readString(Path.of(AuthorizationServerConfig.class.getClassLoader().getResource("privatekey-pkcs8.pem").toURI()));
        } catch (IOException | URISyntaxException ex) {
            log.error("Unable to read public and private key from disk: ", ex);
            throw new RuntimeException(ex);
        }
        String pub = pubText.replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
        String priv = privText.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
        PublicKey pubKey;
        PrivateKey privKey;
        try {
            pubKey = loadPublicKey(pub);
            privKey = loadPrivateKey(priv);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            log.error("Unable to convert public/private pem keys to key pair: ", ex);
            throw new RuntimeException(ex);
        }
        return new RSAKey.Builder((RSAPublicKey) pubKey)
                .privateKey((RSAPrivateKey) privKey)
                .keyID("localkey")
                .build();
    }

    private static PrivateKey loadPrivateKey(String privateKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKeyPem.getBytes());
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedPrivateKey));
    }

    private static PublicKey loadPublicKey(String publicKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedPublicKey = Base64.getDecoder().decode(publicKeyPem.getBytes());
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedPublicKey));
    }

    private static KeyPair createKeyPair(byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048,secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    //@Bean
    //public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
    //    return context -> {
    //        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
    //            Set<String> userAuthorities = context.getPrincipal().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    //            Set<String> authorizedScopes = context.getAuthorizedScopes();
    //            Set<String> claims = new HashSet<>();
    //            claims.addAll(userAuthorities);
    //            claims.addAll(authorizedScopes);
    //            log.info("OAuth2TokenCustomizer :: Adding user claims {}", userAuthorities);
    //            context.getClaims().claims(c -> c.put("scope", claims));
    //        }
    //    };
    //}

    @Bean
    public JdbcOAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository repository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, repository);
    }

    @Bean
    public JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository repository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, repository);
    }

    @Bean
    public TextEncryptor textEncryptor(@Value("${jwt.persistence.password}") String password,
                                       @Value("${jwt.persistence.salt") String salt) {
        return Encryptors.text(password, Hex.encodeHexString(salt.getBytes()));
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> checkRsaKeyPairPresent(ApplicationEventPublisher publisher, JdbcRsaKeyPairRepository repository) {
        return event -> {
            if (repository.findAll().isEmpty()) {
                publisher.publishEvent(new CreateRsaKeyPairEvent("vinny-local-key"));
            }
        };
    }

    @Bean
    ApplicationListener<CreateRsaKeyPairEvent> createRsaKeyPair(ApplicationEventPublisher publisher, JdbcRsaKeyPairRepository repository, KeyGenerator keyGenerator) {
        return event -> {
            String keyId = (String) event.getSource();
            RsaKeyPairRepository.RsaKeyPair rsaKeyPair = keyGenerator.generateKeyPair(keyId);
            repository.save(rsaKeyPair);
            log.info("RSA key pair generated and persisted to server");
        };
    }

}
