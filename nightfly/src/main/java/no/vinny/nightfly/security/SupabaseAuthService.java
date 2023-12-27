package no.vinny.nightfly.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupabaseAuthService {

    JwtUtil jwtUtil;

    public SupabaseAuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public SupabaseUser user(String accessToken) {
        //SecretKey key = jwtUtil.fetchSecretKey();
        byte[] key = jwtUtil.getJwtSecret().getBytes();
        String issuer = jwtUtil.getIssuer();
        try {
            Jwt<JwsHeader, Claims> parse = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer(issuer)
                    .setAllowedClockSkewSeconds(30L) // allow a margin 30 sec difference
                    .build()
                    .parseClaimsJws(accessToken);
            return new SupabaseUser(parse.getBody(), accessToken);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.warn("Unable to parse JWT:", ex);
            return null;
        }
    }
}
