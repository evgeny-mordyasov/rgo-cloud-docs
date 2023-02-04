package rgo.cloud.docs.boot.service;

import rgo.cloud.docs.boot.storage.repository.ReadingDocumentRepository;
import rgo.cloud.docs.internal.api.storage.ReadingDocument;

public class ReadingDocumentService {
    private final ReadingDocumentRepository repository;

    public ReadingDocumentService(ReadingDocumentRepository repository) {
        this.repository = repository;
    }

    public ReadingDocument save(ReadingDocument rd) {
        return repository.save(rd);
    }
}
