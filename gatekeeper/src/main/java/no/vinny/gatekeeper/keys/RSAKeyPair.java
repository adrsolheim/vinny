package no.vinny.gatekeeper.keys;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.Value;

import java.time.Instant;

@Value
public class RSAKeyPair {

    private final RSAKey rsaKey;
    private final Instant created;

    public RSAKeyPair(RSAKey rsaKey, Instant created) {
        this.rsaKey = rsaKey;
        this.created = created;
    }
}
