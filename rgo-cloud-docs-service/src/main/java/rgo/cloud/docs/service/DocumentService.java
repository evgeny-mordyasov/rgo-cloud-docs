package rgo.cloud.docs.service;

import lombok.extern.slf4j.Slf4j;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.docs.db.api.repository.DocumentRepository;
import rgo.cloud.docs.db.api.entity.Document;

import java.util.List;
import java.util.Optional;

@Slf4j
public class DocumentService {
    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public List<Document> findAll() {
        return repository.findAll();
    }

    public Optional<Document> findById(Long entityId) {
        return repository.findById(entityId);
    }

    public Document save(Document document) {
        return repository.save(document);
    }

    public Document patchFileName(Document document) {
        Document fromDb = getById(document.getEntityId());

        Document data = fromDb.toBuilder()
                .fullName(document.getName() + "." + fromDb.getExtension())
                .name(document.getName())
                .build();

        return repository.patchFileName(data);
    }

    public void deleteById(Long entityId) {
        getById(entityId);
        repository.deleteById(entityId);
    }

    private Document getById(Long entityId) {
        Optional<Document> opt = repository.findById(entityId);

        if (opt.isEmpty()) {
            String errorMsg = "The document by id not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }

        return opt.get();
    }
}
