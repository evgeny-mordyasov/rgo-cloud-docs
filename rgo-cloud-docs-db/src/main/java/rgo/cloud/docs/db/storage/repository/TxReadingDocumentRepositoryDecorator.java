package rgo.cloud.docs.db.storage.repository;

import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.db.api.entity.ReadingDocument;
import rgo.cloud.docs.db.api.repository.ReadingDocumentRepository;

public class TxReadingDocumentRepositoryDecorator implements ReadingDocumentRepository {
    private final ReadingDocumentRepository delegate;
    private final DbTxManager tx;

    public TxReadingDocumentRepositoryDecorator(ReadingDocumentRepository delegate, DbTxManager tx) {
        this.delegate = delegate;
        this.tx = tx;
    }

    @Override
    public ReadingDocument save(ReadingDocument rd) {
        return tx.tx(() -> delegate.save(rd));
    }
}
