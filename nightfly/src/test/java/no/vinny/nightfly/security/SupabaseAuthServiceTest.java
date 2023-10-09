package no.vinny.nightfly.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupabaseAuthServiceTest {

    private JwtUtil jwtUtil;
    private SupabaseAuthService supabaseAuthService;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil("K+g7wvB8pP5g53JL3ApQnnEZ9Xr29TqqLl2qloDpahnkXrem43uVMexrE0G+317I4ettbMXETbKrdO+WPKBdTQ==");
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
                .setIssuer("supabase")
                .setSubject("Bob");
        claims.put("role", "USER");
        return claims;
    }

    private String bobUserJwt() {
        return Jwts.builder()
                .setIssuer("supabase")
                .setSubject("Bob")
                .claim("role", "USER")
                .signWith(jwtUtil.fetchSecretKey())
                .compact();
    }
}