package rgo.cloud.docs.db.api.repository;

import rgo.cloud.docs.db.api.entity.Translation;
import rgo.cloud.docs.db.api.entity.TranslationKey;

import java.util.List;
import java.util.Optional;

public interface TranslationRepository {
    List<Translation> findAll();

    List<Translation> findByDocumentId(Long documentId);

    List<Translation> findByClassificationId(Long classificationId);

    List<Translation> findByFullName(String name);

    Optional<Translation> findByKey(TranslationKey key);

    Optional<Translation> findByKeyWithData(TranslationKey key);

    boolean exists(Long entityId);

    boolean exists(TranslationKey key);

    Translation save(Translation translation);

    void deleteById(Long entityId);
}
