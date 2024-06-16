package no.vinny.gatekeeper.keys;

import com.nimbusds.jose.jwk.RSAKey;

import java.time.Instant;

public class RSAKeyPair {

    private final RSAKey rsaKey;
    private final Instant created;

    public RSAKeyPair(RSAKey rsaKey, Instant created) {
        this.rsaKey = rsaKey;
        this.created = created;
    }
}
