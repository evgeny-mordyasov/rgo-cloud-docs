package rgo.cloud.docs.boot.storage.repository;

import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;

import java.util.List;
import java.util.Optional;

public class TxClassificationRepositoryDecorator implements ClassificationRepository {
    private final ClassificationRepository delegate;
    private final DbTxManager tx;

    public TxClassificationRepositoryDecorator(ClassificationRepository delegate, DbTxManager tx) {
        this.delegate = delegate;
        this.tx = tx;
    }

    @Override
    public List<Classification> findAll() {
        return tx.tx(delegate::findAll);
    }

    @Override
    public Optional<Classification> findById(Long entityId) {
        return tx.tx(() -> delegate.findById(entityId));
    }

    @Override
    public Optional<Classification> findByName(String name) {
        return tx.tx(() -> delegate.findByName(name));
    }

    @Override
    public boolean exists(Long entityId) {
        return tx.tx(() -> delegate.exists(entityId));
    }

    @Override
    public Classification save(Classification classification) {
        return tx.tx(() -> delegate.save(classification));
    }

    @Override
    public Classification update(Classification classification) {
        return tx.tx(() -> delegate.update(classification));
    }

    @Override
    public void deleteById(Long entityId) {
        tx.tx(() -> delegate.deleteById(entityId));
    }
}
