package no.vinny.nightfly.batch.impl;

import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchMapper;
import no.vinny.nightfly.batch.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;

public class BatchRepositoryImpl implements BatchRepository {

    private static final String BATCH_COLUMNS = "(id, brewfather_id, name, status)";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BatchRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
       this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(BatchDTO batch) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brewfatherId", batch.getBrewfatherId());
        params.addValue("name", batch.getName());
        params.addValue("status", batch.getStatus().getValue());
        String sql = new StringBuilder()
                .append("INSERT INTO batch ")
                .append(BATCH_COLUMNS)
                .append(" VALUES (NULL, :brewfatherId, :name, :status)")
                .toString();
        return jdbcTemplate.update(sql, params);
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public Optional<Batch> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Batch> findByBrewfatherId(String id) {
        return Optional.empty();
    }

    @Override
    public List<Batch> findAll() {
        String sql = "SELECT " + BATCH_COLUMNS + " FROM batch";
        return jdbcTemplate.query(sql, new BatchMapper());
    }
}
