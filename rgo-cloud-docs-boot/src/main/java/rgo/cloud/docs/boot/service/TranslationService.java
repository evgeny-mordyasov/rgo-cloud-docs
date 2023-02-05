package rgo.cloud.docs.boot.service;

import lombok.extern.slf4j.Slf4j;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.ViolatesConstraintException;
import rgo.cloud.docs.boot.storage.repository.TranslationRepository;
import rgo.cloud.docs.internal.api.storage.Translation;

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

    public Optional<Translation> findByDocumentIdAndLanguageId(Long documentId, Long languageId) {
        return repository.findByDocumentIdAndLanguageId(documentId, languageId);
    }

    public Optional<Translation> findByDocumentIdAndLanguageIdWithData(Long documentId, Long languageId) {
        return repository.findByDocumentIdAndLanguageIdWithData(documentId, languageId);
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
        if (repository.exists(tr.getDocument().getEntityId(), tr.getLanguage().getEntityId())) {
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
