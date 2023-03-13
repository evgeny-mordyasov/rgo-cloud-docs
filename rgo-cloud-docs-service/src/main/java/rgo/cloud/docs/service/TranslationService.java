package rgo.cloud.docs.service;

import lombok.extern.slf4j.Slf4j;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.ViolatesConstraintException;
import rgo.cloud.docs.db.api.entity.TranslationKey;
import rgo.cloud.docs.db.api.repository.TranslationRepository;
import rgo.cloud.docs.db.api.entity.Translation;

import java.util.List;
import java.util.Optional;

@Slf4j
public class TranslationService {
    private final TranslationRepository repository;

    public TranslationService(TranslationRepository repository) {
        this.repository = repository;
    }

    public List<Translation> findAll() {
        return repository.findAll();
    }

    public List<Translation> findByDocumentId(Long documentId) {
        return repository.findByDocumentId(documentId);
    }

    public List<Translation> findByClassificationId(Long classificationId) {
        return repository.findByClassificationId(classificationId);
    }

    public List<Translation> findByFullName(String name) {
        return repository.findByFullName(name);
    }

    public Optional<Translation> findByKey(TranslationKey key) {
        return repository.findByKey(key);
    }

    public Optional<Translation> findByKeyWithData(TranslationKey key) {
        return repository.findByKeyWithData(key);
    }

    public Translation save(Translation tr) {
        checkForDuplicate(tr);
        return repository.save(tr);
    }

    public void deleteById(Long entityId) {
        validate(entityId);
        repository.deleteById(entityId);
    }

    private void checkForDuplicate(Translation tr) {
        if (repository.exists(tr.key())) {
            String errorMsg = "The translation by documentId and languageId already exists.";
            log.error(errorMsg);
            throw new ViolatesConstraintException(errorMsg);
        }
    }

    private void validate(Long entityId) {
        if (!repository.exists(entityId)) {
            String errorMsg = "The translation by id not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }
    }
}
