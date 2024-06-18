package no.vinny.gatekeeper.keys;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcRsaKeyPairRepository implements RsaKeyPairRepository {
    private static final String SELECT_KEY_PAIR = "SELECT id, private_key, public_key, created FROM rsa_key_pairs";
    private static final String INSERT_KEY_PAIR = "INSERT INTO rsa_key_pairs (private_key, public_key, created) VALUES (:privateKey, :publicKey, :created)";
    private final JdbcTemplate jdbcTemplate;
    private final KeyRowMapper rowMapper;

    public JdbcRsaKeyPairRepository(JdbcTemplate jdbcTemplate, RsaPrivateKeyConverter rsaPrivateKeyConverter, RsaPublicKeyConverter rsaPublicKeyConverter) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new KeyRowMapper(rsaPrivateKeyConverter, rsaPublicKeyConverter);
    }

    @Override
    public List<RSAKeyPair> findAll() {
        return null;
    }

    @Override
    public void save(RSAKeyPair rsaKeyPair) {

    }
    
    private static class KeyRowMapper implements RowMapper<RSAKeyPair> {

        private final RsaPrivateKeyConverter rsaPrivateKeyConverter;
        private final RsaPublicKeyConverter rsaPublicKeyConverter;

        private KeyRowMapper(RsaPrivateKeyConverter rsaPrivateKeyConverter, RsaPublicKeyConverter rsaPublicKeyConverter) {
            this.rsaPrivateKeyConverter = rsaPrivateKeyConverter;
            this.rsaPublicKeyConverter = rsaPublicKeyConverter;
        }

        @Override
        public RSAKeyPair mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                RSAKey rsaKey = new RSAKey.Builder(rsaPublicKeyConverter.deserializeFromByteArray(rs.getString("publicKey").getBytes()))
                        .privateKey(rsaPrivateKeyConverter.deserializeFromByteArray(rs.getString("privateKey").getBytes()))
                        .keyID(rs.getString("keyId"))
                        .build();
                return new RSAKeyPair(rsaKey, rs.getTimestamp("created").toInstant());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
