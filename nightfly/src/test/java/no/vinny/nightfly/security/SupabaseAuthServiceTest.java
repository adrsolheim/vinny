package no.vinny.nightfly.security;

import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class SupabaseAuthServiceTest {

    private JwtUtil jwtUtil;
    private SupabaseAuthService supabaseAuthService;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil("K+g7wvB8pP5g53JL3ApQnnEZ9Xr29TqqLl2qloDpahnkXrem43uVMexrE0G+317I4ettbMXETbKrdO+WPKBdTQ==", "http://auth-server");
        supabaseAuthService = new SupabaseAuthService(jwtUtil);
    }

    @Test
    void user_authenticated_and_has_user_role() {
        String accessToken = bobUserJwt();
        SupabaseUser authenticatedUserBob = supabaseAuthService.user(accessToken);

        SupabaseUser expected = new SupabaseUser(bobUserClaims(), accessToken);
        Assertions.assertEquals(expected.getUsername(), authenticatedUserBob.getUsername());
        Assertions.assertEquals(expected.getRole(), authenticatedUserBob.getRole());
    }

    private Claims bobUserClaims() {
        Claims claims = Jwts.claims()
                .setIssuer("https://viatplyztqnkievknofv.supabase.co/auth/v1")
                .setSubject("Bob");
        claims.put("role", "USER");
        return claims;
    }

    private String bobUserJwt() {
        try {
            return Jwts.builder()
                    .setIssuer("http://auth-server")
                    .setSubject("Bob")
                    .claim("role", "USER")
                    .signWith(jwtUtil.getRsaKey().toPrivateKey())
                    .compact();
        } catch (JOSEException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}