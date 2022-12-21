package rgo.cloud.docs.boot.service;

import lombok.extern.slf4j.Slf4j;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.docs.boot.storage.repository.DocumentRepository;
import rgo.cloud.docs.internal.api.storage.Document;

import java.util.Optional;

@Slf4j
public class DocumentService {
    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public Optional<Document> findById(Long entityId) {
        return repository.findById(entityId);
    }

    public Document save(Document document) {
        return repository.save(document);
    }

    public void deleteById(Long entityId) {
        validateDocument(entityId);
        repository.deleteById(entityId);
    }

    private void validateDocument(Long entityId) {
        if (!repository.exists(entityId)) {
            String errorMsg = "The document by id not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }
    }
}
