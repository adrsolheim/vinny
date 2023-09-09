package no.vinny.nightfly.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Slf4j
@Service
public class SupabaseAuthService {

    @Value("supabase.jwt_secret")
    private String jwtSecret;

    public SupabaseUser user(String accessToken) {
        if (jwtSecret == null || "".equals(jwtSecret)) {
            log.warn("Unable to perform authentication: Missing JWT secret. Make sure environment properties are configured and loaded.");
            return null;
        }
        String encodedSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        try {
            Jwt<JwsHeader, Claims> parse = Jwts.parserBuilder().setSigningKey(encodedSecret)
                    .requireAudience("authentication")
                    .build()
                    .parseClaimsJws(accessToken);
            return new SupabaseUser(parse.getBody(), accessToken);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.warn("Unable to parse JWT: {}", ex);
            return null;
        }
    }
}
