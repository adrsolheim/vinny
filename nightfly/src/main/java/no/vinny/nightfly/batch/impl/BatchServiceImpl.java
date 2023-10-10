package no.vinny.nightfly.batch.impl;

import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchRepository;
import no.vinny.nightfly.batch.BatchService;
import no.vinny.nightfly.batch.Mapper;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final Mapper.ToDTO toDTO;

    public BatchServiceImpl(BatchRepository batchRepository, Mapper.ToDTO toDTO) {
        this.batchRepository = batchRepository;
        this.toDTO = toDTO;
    }

    @Override
    public BatchDTO get(Long id) {
        return batchRepository.findById(id).map(toDTO).orElse(null);
    }

    @Override
    public BatchDTO getByBrewfatherId(String brewfatherId) {
        return batchRepository.findByBrewfatherId(brewfatherId).map(toDTO).orElse(null);
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
        return null;
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
    public Long sync() {
        return null;
    }

    @Override
    public BatchDTO update(Long id, BatchDTO batch) {
        return null;
    }

    @Override
    public BatchDTO replace(Long id, BatchDTO batch) {
        return null;
    }
}
