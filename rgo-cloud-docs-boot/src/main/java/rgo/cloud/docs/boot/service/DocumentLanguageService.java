package rgo.cloud.docs.boot.service;

import lombok.extern.slf4j.Slf4j;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.ViolatesConstraintException;
import rgo.cloud.docs.boot.storage.repository.DocumentLanguageRepository;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;

import java.util.List;
import java.util.Optional;

@Slf4j
public class DocumentLanguageService {
    private final DocumentLanguageRepository repository;

    public DocumentLanguageService(DocumentLanguageRepository repository) {
        this.repository = repository;
    }

    public List<DocumentLanguage> findAll() {
        return repository.findAll();
    }

    public List<DocumentLanguage> findByDocumentId(Long documentId) {
        return repository.findByDocumentId(documentId);
    }

    public List<DocumentLanguage> findByClassificationId(Long classificationId) {
        return repository.findByClassificationId(classificationId);
    }

    public Optional<DocumentLanguage> findByDocumentIdAndLanguageId(Long documentId, Long languageId) {
        return repository.findByDocumentIdAndLanguageId(documentId, languageId);
    }

    public Optional<DocumentLanguage> findByDocumentIdAndLanguageIdWithData(Long documentId, Long languageId) {
        return repository.findByDocumentIdAndLanguageIdWithData(documentId, languageId);
    }

    public DocumentLanguage save(DocumentLanguage dl) {
        checkForDuplicate(dl);
        return repository.save(dl);
    }

    public void deleteById(Long entityId) {
        validate(entityId);
        repository.deleteById(entityId);
    }

    private void checkForDuplicate(DocumentLanguage dl) {
        if (repository.exists(dl.getDocument().getEntityId(), dl.getLanguage().getEntityId())) {
            String errorMsg = "The documentLanguage by documentId and languageId already exists.";
            log.error(errorMsg);
            throw new ViolatesConstraintException(errorMsg);
        }
    }

    private void validate(Long entityId) {
        if (!repository.exists(entityId)) {
            String errorMsg = "The documentLanguage by id not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }
    }
}
