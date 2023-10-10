package no.vinny.nightfly.batch;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;
import java.util.Optional;

public interface BatchRepository {
    int insert(BatchDTO batch);

    int delete(Long id);

    Optional<Batch> findById(Long id);

    Optional<Batch> findByBrewfatherId(String id);

    List<Batch> findAll();
}