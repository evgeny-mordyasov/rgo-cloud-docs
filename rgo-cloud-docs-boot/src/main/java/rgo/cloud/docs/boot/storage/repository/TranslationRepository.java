package rgo.cloud.docs.boot.storage.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.TranslationQuery;
import rgo.cloud.docs.boot.storage.query.LanguageQuery;
import rgo.cloud.docs.boot.storage.repository.mapper.LanguageMapper;
import rgo.cloud.docs.internal.api.storage.Translation;
import rgo.cloud.docs.internal.api.storage.Language;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.docs.boot.storage.repository.mapper.TranslationMapper.emptyMapper;
import static rgo.cloud.docs.boot.storage.repository.mapper.TranslationMapper.lazyMapper;
import static rgo.cloud.docs.boot.storage.repository.mapper.TranslationMapper.dataMapper;

@Slf4j
public class TranslationRepository {
    private final DbTxManager tx;
    private final NamedParameterJdbcTemplate jdbc;

    public TranslationRepository(DbTxManager tx) {
        this.tx = tx;
        this.jdbc = tx.jdbc();
    }

    public List<Translation> findAll() {
        List<Translation> translations = tx.tx(() ->
                jdbc.query(TranslationQuery.findAll(), lazyMapper));
        log.info("Size of translations: {}", translations.size());

        return translations;
    }

    public List<Translation> findByDocumentId(Long documentId) {
        MapSqlParameterSource params = new MapSqlParameterSource("document_id", documentId);
        List<Translation> translations = tx.tx(() ->
                jdbc.query(TranslationQuery.findByDocumentId(), params, lazyMapper));
        log.info("Size of translations by documentId='{}': {}", documentId, translations.size());

        return translations;
    }

    public List<Translation> findByClassificationId(Long classificationId) {
        MapSqlParameterSource params = new MapSqlParameterSource("classification_id", classificationId);
        List<Translation> translations = tx.tx(() ->
                jdbc.query(TranslationQuery.findByClassificationId(), params, lazyMapper));
        log.info("Size of translations by classificationId='{}': {}", classificationId, translations.size());

        return translations;
    }

    private Optional<Translation> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(tx.tx(() ->
                jdbc.query(TranslationQuery.findById(), params, emptyMapper)));
    }

    public Optional<Translation> findByDocumentIdAndLanguageId(Long documentId, Long languageId) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", documentId,
                "language_id", languageId));

        return first(tx.tx(() ->
                jdbc.query(TranslationQuery.findByDocumentIdAndLanguageId(), params, lazyMapper)));
    }

    public Optional<Translation> findByDocumentIdAndLanguageIdWithData(Long documentId, Long languageId) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", documentId,
                "language_id", languageId));

        return first(tx.tx(() ->
                jdbc.query(TranslationQuery.findByDocumentIdAndLanguageIdWithData(), params, dataMapper)));
    }

    private Optional<Translation> first(List<Translation> list) {
        if (list.isEmpty()) {
            log.info("The translation not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    public boolean exists(Long entityId) {
        return findById(entityId).isPresent();
    }

    public boolean exists(Long documentId, Long languageId) {
        return findByDocumentIdAndLanguageId(documentId, languageId).isPresent();
    }

    public Translation save(Translation translation) {
        checkInternalEntities(translation.getLanguage().getEntityId());

        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", translation.getDocument().getEntityId(),
                "language_id", translation.getLanguage().getEntityId(),
                "data", translation.getData()));

        return tx.tx(() -> {
            jdbc.update(TranslationQuery.save(), params);
            Optional<Translation> opt =
                    findByDocumentIdAndLanguageId(translation.getDocument().getEntityId(), translation.getLanguage().getEntityId());

            if (opt.isEmpty()) {
                String errorMsg = "Translation save error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            return opt.get();
        });
    }

    private void checkInternalEntities(Long languageId) {
        List<Language> rs = tx.tx(() ->
                jdbc.query(LanguageQuery.findById(),
                        new MapSqlParameterSource("entity_id", languageId),
                        LanguageMapper.mapper));

        if (rs.isEmpty()) {
            throw new EntityNotFoundException("Language by id not found.");
        }
    }

    public void deleteById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);

        tx.tx(() -> {
            int result = jdbc.update(TranslationQuery.deleteById(), params);

            if (result == 0) {
                String errorMsg = "Translation delete error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }
        });
    }
}
