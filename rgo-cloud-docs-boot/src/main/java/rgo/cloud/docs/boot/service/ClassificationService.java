package rgo.cloud.docs.boot.service;

import lombok.extern.slf4j.Slf4j;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.ViolatesConstraintException;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;
import rgo.cloud.docs.db.api.entity.Classification;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ClassificationService {
    private final ClassificationRepository repository;

    public ClassificationService(ClassificationRepository repository) {
        this.repository = repository;
    }

    public List<Classification> findAll() {
        return repository.findAll();
    }

    public Optional<Classification> findById(Long entityId) {
        return repository.findById(entityId);
    }

    public Optional<Classification> findByName(String name) {
        return repository.findByName(name);
    }

    public Classification save(Classification classification) {
        checkNameForDuplicate(classification.getName());
        return repository.save(classification);
    }

    public Classification update(Classification classification) {
        validateClassification(classification.getEntityId());
        return repository.update(classification);
    }

    public void deleteById(Long entityId) {
        validateClassification(entityId);
        repository.deleteById(entityId);
    }

    private void checkNameForDuplicate(String name) {
        repository.findByName(name).ifPresent(ignored -> {
            String errorMsg = "Classification by name already exist.";
            log.error(errorMsg);
            throw new ViolatesConstraintException(errorMsg);
        });
    }

    private void validateClassification(Long entityId) {
        if (!repository.exists(entityId)) {
            String errorMsg = "The classification by id not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }
    }
}
