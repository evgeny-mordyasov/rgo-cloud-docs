package rgo.cloud.docs.service;

import rgo.cloud.docs.db.api.repository.ReadingDocumentRepository;
import rgo.cloud.docs.db.api.entity.ReadingDocument;

public class ReadingDocumentService {
    private final ReadingDocumentRepository repository;

    public ReadingDocumentService(ReadingDocumentRepository repository) {
        this.repository = repository;
    }

    public ReadingDocument save(ReadingDocument rd) {
        return repository.save(rd);
    }
}
