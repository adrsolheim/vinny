package no.vinny.nightfly.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static java.util.Collections.unmodifiableMap;

public class SupabaseUser implements UserDetails {
    private final List<GrantedAuthority> grantedAuthorities;
    private final Claims claims;
    //private final Map<String, String> appMetadata;
    //private final Map<String, String> userMetadata;
    private final String token;
    private final String id;
    private final String role;
    private final String issuer;
    //private final String role;
    //private final String email;
    //private final String phone;

    //private final String avatarUrl;
    //private final String userName;
    //private final String fullName;
    //private final String emailConfirmedAt;
    //private final String confirmedAt;
    //private final String lastSignInAt;
    //private final String createdAt;
    //private final String updatedAt;
    //private final String provider;

    public SupabaseUser(Claims claims, String accessToken) {
        grantedAuthorities = new ArrayList<>();
        this.claims = claims;
        token = accessToken;
        //appMetadata = (Map<String, String>) unmodifiableMap(claims.get("app_metadata", HashMap.class));
        //userMetadata = (Map<String, String>) unmodifiableMap(claims.get("user_metadata", HashMap.class));

        // Registered Claims
        issuer = claims.get("iss", String.class);
        id = claims.get("sub", String.class);
        role = claims.get("role", String.class);
        claims.get("exp", Integer.class);
        claims.get("iat", Integer.class);
        //provider = appMetadata.getOrDefault("provider", "");

        // Jose Header
        claims.get("typ", String.class);

        // Claims
        //role = claims.get("role", String.class);
        //email = claims.get("email", String.class);
        //phone = claims.get("phone", String.class);

        //avatarUrl = userMetadata.containsKey("avatar_url") ? userMetadata.get("avatar_url") : null; // default url?
        //fullName = userMetadata.get("full_name");
        //userName = userMetadata.containsKey("user_name") ? userMetadata.get("user_name") : email;
        //emailConfirmedAt = userMetadata.get("email_confirmed_at");
        //confirmedAt = userMetadata.get("confirmed_ad");
        //lastSignInAt = userMetadata.get("last_sign_in_at");
        //createdAt = userMetadata.get("created_at");
        //updatedAt = userMetadata.get("updated_ad");

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
