package no.vinny.gatekeeper.keys;

import java.util.List;

public interface RsaKeyPairRepository {
    List<RSAKeyPair> findAll();
    void save(RSAKeyPair rsaKeyPair);
}
