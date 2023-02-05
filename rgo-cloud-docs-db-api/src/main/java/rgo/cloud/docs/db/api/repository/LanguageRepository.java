package rgo.cloud.docs.db.api.repository;

import rgo.cloud.docs.db.api.entity.Language;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository {
    List<Language> findAll();

    Optional<Language> findById(Long entityId);

    Optional<Language> findByName(String name);

    boolean exists(Long entityId);

    Language save(Language language);

    Language update(Language language);
}
