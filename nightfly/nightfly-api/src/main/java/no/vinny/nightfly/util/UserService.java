package no.vinny.nightfly.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    public Optional<String> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // bootstrap/startup, public endpoint, tests, async/reactive context
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return Optional.ofNullable(jwt.getSubject());
        }
        if (principal instanceof UserDetails userDetails) {
            return Optional.ofNullable(userDetails.getUsername());
        }

        return Optional.empty();
    }

    public String currentUserOrThrow() {
        return currentUser().orElseThrow(() -> new IllegalStateException("No user authenticated"));
    }
}
