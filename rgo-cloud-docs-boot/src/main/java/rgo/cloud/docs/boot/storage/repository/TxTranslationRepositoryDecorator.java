package rgo.cloud.docs.boot.storage.repository;

import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.db.api.entity.Translation;
import rgo.cloud.docs.db.api.entity.TranslationKey;
import rgo.cloud.docs.db.api.repository.TranslationRepository;

import java.util.List;
import java.util.Optional;

public class TxTranslationRepositoryDecorator implements TranslationRepository {
    private final TranslationRepository delegate;
    private final DbTxManager tx;

    public TxTranslationRepositoryDecorator(TranslationRepository delegate, DbTxManager tx) {
        this.delegate = delegate;
        this.tx = tx;
    }

    @Override
    public List<Translation> findAll() {
        return tx.tx(delegate::findAll);
    }

    @Override
    public List<Translation> findByDocumentId(Long documentId) {
        return tx.tx(() -> delegate.findByDocumentId(documentId));
    }

    @Override
    public List<Translation> findByClassificationId(Long classificationId) {
        return tx.tx(() -> delegate.findByClassificationId(classificationId));
    }

    @Override
    public Optional<Translation> findByKey(TranslationKey key) {
        return tx.tx(() -> delegate.findByKey(key));
    }

    @Override
    public Optional<Translation> findByKeyWithData(TranslationKey key) {
        return tx.tx(() -> delegate.findByKeyWithData(key));
    }

    @Override
    public boolean exists(Long entityId) {
        return tx.tx(() -> delegate.exists(entityId));
    }

    @Override
    public boolean exists(TranslationKey key) {
        return tx.tx(() -> delegate.exists(key));
    }

    @Override
    public Translation save(Translation translation) {
        return tx.tx(() -> delegate.save(translation));
    }

    @Override
    public void deleteById(Long entityId) {
        tx.tx(() -> delegate.deleteById(entityId));
    }
}
