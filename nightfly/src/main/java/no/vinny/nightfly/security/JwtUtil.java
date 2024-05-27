package no.vinny.nightfly.security;

import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

    private String jwtSecret;
    private String issuer;
    private PubKey pubKey;

    public JwtUtil(@Value("${spring.security.oauth2.client.registration.nightfly.client-secret}") String jwtSecret,
                   @Value("${spring.security.oauth2.client.provider.spring.issuer-uri}") String issuer,
                   PubKey pubKey) {
        this.jwtSecret = jwtSecret;
        this.issuer = issuer;
        if (pubKey == null) {
            throw new RuntimeException("Missing environment data: public key");
        }
        this.pubKey = pubKey;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public String getIssuer() {
        return issuer;
    }

    public String fetchSecretKeyString() {
        log.info("jwt_secret: {}", jwtSecret);
        if (jwtSecret == null || "".equals(jwtSecret)) {
            log.warn("Unable to perform authentication: Missing JWT secret. Make sure environment properties are configured and loaded.");
            return null;
        }
        return generateSecretKeyStringFrom(jwtSecret.getBytes());
    }

    public PublicKey getPublicKey() {
        String formattedKey = pubKey.getPublicKey().replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decodedKey = Base64.getDecoder().decode(formattedKey);
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateSecretKeyStringFrom(byte[] secret) {
        return Base64.getEncoder().encodeToString(secret);
    }

    public SecretKey fetchSecretKey() {
        if (jwtSecret == null || "".equals(jwtSecret)) {
            log.warn("Unable to perform authentication: Missing JWT secret. Make sure environment properties are configured and loaded.");
            return null;
        }
        return generateSecretKeyFrom(jwtSecret.getBytes());
    }

    private SecretKey generateSecretKeyFrom(byte[] secret) {
        return Keys.hmacShaKeyFor(secret);
    }

    public RSAKey getRsaKey() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return createRsaKey(jwtSecret.getBytes("utf-8"));
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

    private static KeyPair createKeyPair(byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048,secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    private PublicKey loadPublicKey() {
        String pubText;
        try {
            pubText = Files.readString(Path.of(JwtUtil.class.getClassLoader().getResource("publickey.pem").toURI()));
            pubText = pubText.replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
        } catch (IOException | URISyntaxException ex) {
            log.error("Unable to read public key 'publickey.pem' from file: ", ex);
            throw new RuntimeException(ex);
        }
        PublicKey publicKey;
        try {
             publicKey = loadPublicKey(pubText);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            log.error("Unable to decode and convert 'publickey.pem' to public key: ", ex);
            throw new RuntimeException(ex);
        }
        return publicKey;
    }
    private PublicKey loadPublicKey(String publicKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedPublicKey = Base64.getDecoder().decode(publicKeyPem.getBytes(StandardCharsets.UTF_8));
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedPublicKey));
    }
}
