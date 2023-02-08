package rgo.cloud.docs.db.storage.repository;

import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.db.api.entity.Document;
import rgo.cloud.docs.db.api.repository.DocumentRepository;

import java.util.List;
import java.util.Optional;

public class TxDocumentRepositoryDecorator implements DocumentRepository {
    private final DocumentRepository delegate;
    private final DbTxManager tx;

    public TxDocumentRepositoryDecorator(DocumentRepository delegate, DbTxManager tx) {
        this.delegate = delegate;
        this.tx = tx;
    }

    @Override
    public List<Document> findAll() {
        return tx.tx(delegate::findAll);
    }

    @Override
    public Optional<Document> findById(Long entityId) {
        return tx.tx(() -> delegate.findById(entityId));
    }

    @Override
    public boolean exists(Long entityId) {
        return tx.tx(() -> delegate.exists(entityId));
    }

    @Override
    public Document save(Document document) {
        return tx.tx(() -> delegate.save(document));
    }

    @Override
    public void deleteById(Long entityId) {
        tx.tx(() -> delegate.deleteById(entityId));
    }
}
