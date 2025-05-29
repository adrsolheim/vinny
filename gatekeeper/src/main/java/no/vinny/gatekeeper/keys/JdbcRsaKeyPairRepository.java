package no.vinny.gatekeeper.keys;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
public class JdbcRsaKeyPairRepository implements RsaKeyPairRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final KeyRowMapper rowMapper;
    private final RsaPrivateKeyConverter rsaPrivateKeyConverter;
    private final RsaPublicKeyConverter rsaPublicKeyConverter;

    public JdbcRsaKeyPairRepository(NamedParameterJdbcTemplate jdbcTemplate, RsaPrivateKeyConverter rsaPrivateKeyConverter, RsaPublicKeyConverter rsaPublicKeyConverter) {
        this.jdbcTemplate = jdbcTemplate;
        this.rsaPrivateKeyConverter = rsaPrivateKeyConverter;
        this.rsaPublicKeyConverter = rsaPublicKeyConverter;
        this.rowMapper = new KeyRowMapper(rsaPrivateKeyConverter, rsaPublicKeyConverter);
    }

    @Override
    public List<RsaKeyPair> findAll() {
        return jdbcTemplate.query("SELECT id, private_key, public_key, created FROM rsa_key_pairs", rowMapper);
    }

    @Override
    public void save(RsaKeyPair rsaKeyPair) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        try(ByteArrayOutputStream privateKey = new ByteArrayOutputStream(); ByteArrayOutputStream publicKey = new ByteArrayOutputStream()) {
            rsaPrivateKeyConverter.serialize(rsaKeyPair.privateKey(), privateKey);
            rsaPublicKeyConverter.serialize(rsaKeyPair.publicKey(), publicKey);
            params.addValue("id", rsaKeyPair.id());
            params.addValue("privateKey", privateKey.toString());
            params.addValue("publicKey", publicKey.toString());
            params.addValue("created", Instant.now());
            jdbcTemplate.update("INSERT INTO rsa_key_pairs (id, private_key, public_key, created) VALUES (:id, :privateKey, :publicKey, :created)", params);
        } catch (IOException ex) {
            throw new IllegalStateException("Caught exception persisting rsa key ", ex);
        }
    }
    
    private static class KeyRowMapper implements RowMapper<RsaKeyPair> {

        private final RsaPrivateKeyConverter rsaPrivateKeyConverter;
        private final RsaPublicKeyConverter rsaPublicKeyConverter;

        private KeyRowMapper(RsaPrivateKeyConverter rsaPrivateKeyConverter, RsaPublicKeyConverter rsaPublicKeyConverter) {
            this.rsaPrivateKeyConverter = rsaPrivateKeyConverter;
            this.rsaPublicKeyConverter = rsaPublicKeyConverter;
        }

        @Override
        public RsaKeyPair mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                return new RsaKeyPair(rs.getString("id"), rs.getTimestamp("created").toInstant(), rsaPublicKeyConverter.deserializeFromByteArray(rs.getString("public_key").getBytes()), rsaPrivateKeyConverter.deserializeFromByteArray(rs.getString("private_key").getBytes()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
