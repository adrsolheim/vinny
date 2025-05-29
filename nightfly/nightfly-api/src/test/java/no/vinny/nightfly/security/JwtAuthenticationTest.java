package no.vinny.nightfly.security;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

//@TestPropertySource(properties = {
//        "supabase.jwt_secret=onetwothreefourfivesixseveneightnine"
//})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtAuthenticationTest {


    private Date TWO_DAYS_AGO;
    private Date TODAY;
    private Date IN_TWO_DAYS;

    //@Value("${supabase.jwt_secret}")
    private String jwtSecret;
    private Instant now;
    private SecretKey key;
    private String jwt;

    @BeforeAll
    void setup() {
        this.now = Instant.parse("2030-12-03T10:15:30.00Z");
        this.TWO_DAYS_AGO = todayPlusDays(-2);
        this.TODAY        = todayPlusDays(0);
        this.IN_TWO_DAYS  = todayPlusDays(2);

        this.jwtSecret = "onetwothreefourfivesixseveneightnine";
        this.key = generateSecretKeyFrom(Base64.getEncoder().encode(jwtSecret.getBytes()));
        this.jwt = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setId(UUID.randomUUID().toString())
                .setSubject("Fred")
                .setIssuer("Supabase Test")
                .setAudience("Unit Test")
                .setExpiration(IN_TWO_DAYS)
                .setIssuedAt(TODAY)
                .claim("role", "USER")
                .signWith(this.key)
                .compact();
    }

    private Date todayPlusDays(int days) {
        long daysInSeconds = days == 0 ? 86400L : 86400L*days;
        return Date.from(this.now.plusSeconds(daysInSeconds));
    }

    private SecretKey generateSecretKeyFrom(byte[] secret) {
        //return Keys.hmacShaKeyFor(secret);
        return new SecretKeySpec(secret, "HmacSHA256");
    }

    @Test
    public void parseToken() {
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(this.key)
                    .build()
                    .parseClaimsJws(this.jwt);
        } catch (JwtException jwtException) {
            jwtException.printStackTrace();
            jws = null;
        }
        Claims claims = jws.getBody();
        Assertions.assertNotNull(jws);
        Assertions.assertEquals("Fred", claims.getSubject());
        Assertions.assertEquals("Supabase Test", claims.getIssuer());
        Assertions.assertEquals("Unit Test", claims.getAudience());
        Assertions.assertEquals(IN_TWO_DAYS, claims.getExpiration());
        Assertions.assertEquals(TODAY, claims.getIssuedAt());
        Assertions.assertEquals("USER", claims.get("role"));
    }

    @Test
    void user() {
    }
}