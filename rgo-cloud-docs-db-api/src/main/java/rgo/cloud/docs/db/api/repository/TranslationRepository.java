package rgo.cloud.docs.db.api.repository;

import rgo.cloud.docs.db.api.entity.Translation;

import java.util.List;
import java.util.Optional;

public interface TranslationRepository {
    List<Translation> findAll();

    List<Translation> findByDocumentId(Long documentId);

    List<Translation> findByClassificationId(Long classificationId);

    Optional<Translation> findByDocumentIdAndLanguageId(Long documentId, Long languageId);

    Optional<Translation> findByDocumentIdAndLanguageIdWithData(Long documentId, Long languageId);

    boolean exists(Long entityId);

    boolean exists(Long documentId, Long languageId);

    Translation save(Translation translation);

    void deleteById(Long entityId);
}
