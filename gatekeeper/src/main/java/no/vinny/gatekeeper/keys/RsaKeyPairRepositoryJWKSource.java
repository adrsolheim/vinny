package no.vinny.gatekeeper.keys;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class RsaKeyPairRepositoryJWKSource implements OAuth2TokenCustomizer<JwtEncodingContext>, JWKSource<SecurityContext> {

    private final JdbcRsaKeyPairRepository jdbcRsaKeyPairRepository;

    public RsaKeyPairRepositoryJWKSource(JdbcRsaKeyPairRepository jdbcRsaKeyPairRepository) {
        this.jdbcRsaKeyPairRepository = jdbcRsaKeyPairRepository;
    }

    @Override
    public List<JWK> get(JWKSelector jwkSelector, SecurityContext securityContext) throws KeySourceException {
        List<RsaKeyPairRepository.RsaKeyPair> keyPairs = jdbcRsaKeyPairRepository.findAll();
        return keyPairs.stream()
                    .map(keyPair -> new RSAKey.Builder(keyPair.publicKey())
                                    .privateKey(keyPair.privateKey())
                                    .keyID(keyPair.id())
                                    .build())
                    .filter(jwkSelector.getMatcher()::matches)
                    .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
            Set<String> userAuthorities = context.getPrincipal().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            Set<String> authorizedScopes = context.getAuthorizedScopes();
            Set<String> claims = new HashSet<>();
            claims.addAll(userAuthorities);
            claims.addAll(authorizedScopes);
            log.info("OAuth2TokenCustomizer :: Adding user claims {}", userAuthorities);
            context.getClaims().claims(c -> c.put("scope", claims));
        }
        List<RsaKeyPairRepository.RsaKeyPair> keyPairs = jdbcRsaKeyPairRepository.findAll();
        if (!keyPairs.isEmpty()) {
            context.getJwsHeader().keyId(keyPairs.get(0).id());
        }
    }
}
