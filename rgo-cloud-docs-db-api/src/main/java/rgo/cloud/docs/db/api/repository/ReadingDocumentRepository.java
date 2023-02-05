package rgo.cloud.docs.db.api.repository;

import rgo.cloud.docs.internal.api.storage.ReadingDocument;

public interface ReadingDocumentRepository {
    ReadingDocument save(ReadingDocument rd);
}
