package rgo.cloud.docs.db.storage.repository;

import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.docs.db.api.repository.LanguageRepository;

import java.util.List;
import java.util.Optional;

public class TxLanguageRepositoryDecorator implements LanguageRepository {
    private final LanguageRepository delegate;
    private final DbTxManager tx;

    public TxLanguageRepositoryDecorator(LanguageRepository delegate, DbTxManager tx) {
        this.delegate = delegate;
        this.tx = tx;
    }

    @Override
    public List<Language> findAll() {
        return tx.tx(delegate::findAll);
    }

    @Override
    public Optional<Language> findById(Long entityId) {
        return tx.tx(() -> delegate.findById(entityId));
    }

    @Override
    public Optional<Language> findByName(String name) {
        return tx.tx(() -> delegate.findByName(name));
    }

    @Override
    public boolean exists(Long entityId) {
        return tx.tx(() -> delegate.exists(entityId));
    }

    @Override
    public Language save(Language language) {
        return tx.tx(() -> delegate.save(language));
    }

    @Override
    public Language update(Language language) {
        return tx.tx(() -> delegate.update(language));
    }
}
