package rgo.cloud.docs.db.api.repository;

import rgo.cloud.docs.internal.api.storage.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {
    List<Document> findAll();

    Optional<Document> findById(Long entityId);

    boolean exists(Long entityId);

    Document save(Document document);

    void deleteById(Long entityId);
}
