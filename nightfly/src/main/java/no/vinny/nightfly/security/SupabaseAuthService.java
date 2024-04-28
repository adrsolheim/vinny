package no.vinny.nightfly.security;

import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

@Slf4j
@Service
public class SupabaseAuthService {

    JwtUtil jwtUtil;
    JwtParser jwtParser;

    public SupabaseAuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        PublicKey publicKey = jwtUtil.getPublicKey();

        this.jwtParser = Jwts.parserBuilder().setSigningKey(publicKey).build();
    }

    public SupabaseUser user(String accessToken) {
        //SecretKey key = jwtUtil.fetchSecretKey();
        byte[] key = jwtUtil.getJwtSecret().getBytes();
        String jwtSecret = Base64.getEncoder().encode(jwtUtil.getJwtSecret().getBytes()).toString();
        String issuer = jwtUtil.getIssuer();
        try {
            Jwt<JwsHeader, Claims> parse = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getRsaKey().toPrivateKey())
                    .requireIssuer(issuer)
                    .setAllowedClockSkewSeconds(30L) // allow a margin 30 sec difference
                    .build()
                    .parseClaimsJws(accessToken);

            log.info("parse: {}", parse);
            SupabaseUser supabaseUser = new SupabaseUser(parse.getBody(), accessToken);
            log.info("user: {}", supabaseUser);
            return supabaseUser;
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.warn("Unable to parse JWT:", ex);
            return null;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
