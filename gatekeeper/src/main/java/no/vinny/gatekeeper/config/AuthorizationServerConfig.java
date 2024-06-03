package no.vinny.gatekeeper.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class AuthorizationServerConfig {

    @Autowired
    private NightflySettings nightflySettings;

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
                                new LoginUrlAuthenticationEntryPoint("/login")
                                //new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // User standard login form
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder, NightflySettings nightflySettings) {
        RegisteredClient simpleClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("simpleclient")
                .clientSecret(passwordEncoder.encode("simpleclient"))
                .redirectUri("http://127.0.0.1:8082/login/oauth2/code/simpleclient-oidc")
                .redirectUri("http://127.0.0.1:8082/authorized")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("batches.read")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        RegisteredClient nightflyClient = RegisteredClient
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
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        return new InMemoryRegisteredClientRepository(simpleClient, nightflyClient);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://gatekeeper:9000")
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //byte[] seed = MessageDigest.getInstance("SHA-256").digest(nightflySettings.getSecret().getBytes("utf-8"));
        //RSAKey rsaKey = createRsaKey(seed);
        RSAKey rsaKey = loadRsaKeyPair();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

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

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                Set<String> userAuthorities = context.getPrincipal().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                Set<String> authorizedScopes = context.getAuthorizedScopes();
                Set<String> claims = new HashSet<>();
                claims.addAll(userAuthorities);
                claims.addAll(authorizedScopes);
                log.info("OAuth2TokenCustomizer :: Adding user claims {}", userAuthorities);
                context.getClaims().claims(c -> c.put("scope", claims));
            }
        };
    }
}
