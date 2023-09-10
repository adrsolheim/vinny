package no.vinny.nightfly.security;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@Component
public class JwtUtil {

    @Value("${supabase.jwt_secret}")
    private String jwtSecret;

    public String fetchSecretKeyString() {
        log.info("jwt_secret: {}", jwtSecret);
        if (jwtSecret == null || "".equals(jwtSecret)) {
            log.warn("Unable to perform authentication: Missing JWT secret. Make sure environment properties are configured and loaded.");
            return null;
        }
        return generateSecretKeyStringFrom(jwtSecret.getBytes());
    }

    private String generateSecretKeyStringFrom(byte[] secret) {
        return Base64.getEncoder().encodeToString(secret);
    }

    public SecretKey fetchSecretKey() {
        log.info("jwt_secret: {}", jwtSecret);
        if (jwtSecret == null || "".equals(jwtSecret)) {
            log.warn("Unable to perform authentication: Missing JWT secret. Make sure environment properties are configured and loaded.");
            return null;
        }
        return generateSecretKeyFrom(Base64.getEncoder().encode(jwtSecret.getBytes()));
    }

    private SecretKey generateSecretKeyFrom(byte[] secret) {
        return Keys.hmacShaKeyFor(secret);
    }
}
