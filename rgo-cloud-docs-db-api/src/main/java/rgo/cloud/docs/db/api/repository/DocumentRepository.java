package rgo.cloud.docs.db.api.repository;

import rgo.cloud.docs.db.api.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {
    List<Document> findAll();

    Optional<Document> findById(Long entityId);

    Document save(Document document);

    Document patchFileName(Document document);

    void deleteById(Long entityId);
}
