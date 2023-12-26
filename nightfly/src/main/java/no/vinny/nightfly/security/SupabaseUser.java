package no.vinny.nightfly.security;

import io.jsonwebtoken.Claims;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static java.util.Collections.unmodifiableMap;

@ToString
public class SupabaseUser implements UserDetails {
    private final List<GrantedAuthority> grantedAuthorities;
    private final Claims claims;
    //private final Map<String, String> appMetadata;
    //private final Map<String, String> userMetadata;
    private final String token;
    private final String id;
    private final String role;
    private final String issuer;
    private final String email;
    private final ZonedDateTime issued;
    private final ZonedDateTime expired;

    public SupabaseUser(Claims claims, String accessToken) {
        grantedAuthorities = new ArrayList<>();
        this.claims = claims;
        token = accessToken;

        // Registered Claims
        issuer = claims.get("iss", String.class);
        id = claims.get("sub", String.class);
        role = claims.get("role", String.class);
        expired = Optional.ofNullable(claims.get("exp", Long.class))
                .map(Instant::ofEpochSecond)
                .map(instant -> instant.atZone(ZoneId.of("Europe/Oslo")))
                .orElse(null);
        issued = Optional.ofNullable(claims.get("exp", Long.class))
                .map(Instant::ofEpochSecond)
                .map(instant -> instant.atZone(ZoneId.of("Europe/Oslo")))
                .orElse(null);
        email = claims.get("email", String.class);

        // Jose Header
        claims.get("typ", String.class);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return token;
    }

    @Override
    public String getUsername() {
        return claims.getSubject();
    }

    public String getIssuer() {
        return issuer;
    }

    public ZonedDateTime getIssued() {
        return issued;
    }

    public ZonedDateTime getExpired() {
        return expired;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
