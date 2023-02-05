package rgo.cloud.docs.db.api.repository;

import rgo.cloud.docs.db.api.entity.ReadingDocument;

public interface ReadingDocumentRepository {
    ReadingDocument save(ReadingDocument rd);
}
