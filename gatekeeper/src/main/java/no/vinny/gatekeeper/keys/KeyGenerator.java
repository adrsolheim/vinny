package no.vinny.gatekeeper.keys;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import no.vinny.gatekeeper.config.ServiceSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

@Slf4j
@Component
public class KeyGenerator {

    private final ServiceSettings settings;

    public KeyGenerator(ServiceSettings settings) {
        this.settings = settings;
    }

    public RsaKeyPairRepository.RsaKeyPair generateKeyPair(String keyId) {
        byte[] seed = generateSeed();
        RSAKey rsaKey = createRsaKey(seed, keyId);
        try {
            return new RsaKeyPairRepository.RsaKeyPair(keyId, Instant.now(), rsaKey.toRSAPublicKey(), rsaKey.toRSAPrivateKey());
        } catch (JOSEException ex) {
            throw new RuntimeException(ex);
        }
    }

    private byte[] generateSeed() {
        if (settings == null || !StringUtils.hasText(settings.getSecret("nightfly"))) {
            log.error(">> KeyGenerator :: Application secret could not be loaded. Ensure secret is configured");
            throw new RuntimeException(">> KeyGenerator :: Application secret could not be loaded. Ensure secret is configured");
        }
        try {
            return MessageDigest.getInstance("SHA-256").digest(settings.getSecret("nightfly").getBytes("utf-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex ) {
            log.error(">> KeyGenerator :: ", ex);
            throw new RuntimeException(ex);
        }
    }
    private RSAKey createRsaKey(byte[] seed, String keyId) {
        KeyPair keyPair = null;
        try {
            keyPair = createKeyPair(seed);
        } catch (NoSuchAlgorithmException ex) {
            log.error(">> KeyGenerator :: ", ex);
            throw new RuntimeException(ex);
        }

        RSAPublicKey publicKey   = (RSAPublicKey)  keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(keyId)
                .build();
    }

    private KeyPair createKeyPair(byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }
}
