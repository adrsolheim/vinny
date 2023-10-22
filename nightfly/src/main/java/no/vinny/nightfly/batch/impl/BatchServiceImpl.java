package no.vinny.nightfly.batch.impl;

import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchRepository;
import no.vinny.nightfly.batch.BatchService;
import no.vinny.nightfly.batch.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("BatchService")
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final Mapper.ToDTO toDTO;

    public BatchServiceImpl(BatchRepository batchRepository, Mapper.ToDTO toDTO) {
        this.batchRepository = batchRepository;
        this.toDTO = toDTO;
    }

    @Override
    public Optional<BatchDTO> get(Long id) {
        return batchRepository.findById(id).map(toDTO);
    }

    @Override
    public Optional<BatchDTO> getByBrewfatherId(String brewfatherId) {
        return batchRepository.findByBrewfatherId(brewfatherId).map(toDTO);
    }

    @Override
    public int add(BatchDTO batch) {
        return batchRepository.insert(batch);
    }

    // TODO: Return pagedlist
    @Override
    public List<BatchDTO> getAll(Pageable pageable) {
        return batchRepository.findAll().stream()
                .map(toDTO)
                .collect(Collectors.toList());
    }

    //TODO: implement rest
    @Override
    public Long count() {
        return batchRepository.count();
    }

    @Override
    public int delete(Long id) {
        return batchRepository.delete(id);
    }

    @Override
    public Long deleteAll() {
        return null;
    }

    @Override
    public BatchDTO update(BatchDTO batch) {
        if (batch.getId() == null) {
            throw new IllegalArgumentException("Batch id must be present in order to find and update batch");
        }
        Optional<BatchDTO> batchDTO = get(batch.getId());
        if (batchDTO.isEmpty()) {
            log.info("UPDATE: Batch not found. Skipping update..");
            return null;
        }
        batchRepository.update(batch);
        return get(batch.getId()).orElse(null); // TODO: throw exception with description here
    }

    @Override
    public BatchDTO upsert(BatchDTO batch) {
        if (batch.getId() != null) {
            return update(batch);
        }
        BatchDTO updated = getByBrewfatherId(batch.getBrewfatherId()).map(this::update).orElse(null);
        if (updated == null) {
            return add(batch);
        }
        return updated;
    }

    @Override
    public BatchDTO replace(Long id, BatchDTO batch) {
        return null;
    }
}
