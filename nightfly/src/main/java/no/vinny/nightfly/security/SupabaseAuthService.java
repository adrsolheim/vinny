package no.vinny.nightfly.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Slf4j
@Service
public class SupabaseAuthService {

    JwtUtil jwtUtil;

    public SupabaseAuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public SupabaseUser user(String accessToken) {
        SecretKey key = jwtUtil.fetchSecretKey();
        String keyString = jwtUtil.fetchSecretKeyString();
        try {
            Jwt<JwsHeader, Claims> parse = Jwts.parserBuilder()
                    .setSigningKey(keyString)
                    .requireIssuer("supabase")
                    .setAllowedClockSkewSeconds(30L) // allow a margin 30 sec difference
                    .build()
                    .parseClaimsJws(accessToken);
            return new SupabaseUser(parse.getBody(), accessToken);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.warn("Unable to parse JWT: {}", ex);
            return null;
        }
    }
}
