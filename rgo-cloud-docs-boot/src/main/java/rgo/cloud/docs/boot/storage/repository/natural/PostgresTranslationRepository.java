package rgo.cloud.docs.boot.storage.repository.natural;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.TranslationQuery;
import rgo.cloud.docs.boot.storage.query.LanguageQuery;
import rgo.cloud.docs.boot.storage.repository.natural.mapper.LanguageMapper;
import rgo.cloud.docs.db.api.entity.Translation;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.docs.db.api.repository.TranslationRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.common.api.util.ExceptionUtil.unpredictableError;
import static rgo.cloud.docs.boot.storage.repository.natural.mapper.TranslationMapper.emptyMapper;
import static rgo.cloud.docs.boot.storage.repository.natural.mapper.TranslationMapper.lazyMapper;
import static rgo.cloud.docs.boot.storage.repository.natural.mapper.TranslationMapper.dataMapper;

@Slf4j
public class PostgresTranslationRepository implements TranslationRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresTranslationRepository(DbTxManager tx) {
        this.jdbc = tx.jdbc();
    }

    @Override
    public List<Translation> findAll() {
        List<Translation> translations = jdbc.query(TranslationQuery.findAll(), lazyMapper);
        log.info("Size of translations: {}", translations.size());

        return translations;
    }

    @Override
    public List<Translation> findByDocumentId(Long documentId) {
        MapSqlParameterSource params = new MapSqlParameterSource("document_id", documentId);
        List<Translation> translations = jdbc.query(
                TranslationQuery.findByDocumentId(), params, lazyMapper);
        log.info("Size of translations by documentId='{}': {}", documentId, translations.size());

        return translations;
    }

    @Override
    public List<Translation> findByClassificationId(Long classificationId) {
        MapSqlParameterSource params = new MapSqlParameterSource("classification_id", classificationId);
        List<Translation> translations = jdbc.query(
                TranslationQuery.findByClassificationId(), params, lazyMapper);
        log.info("Size of translations by classificationId='{}': {}", classificationId, translations.size());

        return translations;
    }

    private Optional<Translation> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(
                jdbc.query(TranslationQuery.findById(), params, emptyMapper));
    }

    @Override
    public Optional<Translation> findByDocumentIdAndLanguageId(Long documentId, Long languageId) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", documentId,
                "language_id", languageId));

        return first(
                jdbc.query(TranslationQuery.findByDocumentIdAndLanguageId(), params, lazyMapper));
    }

    @Override
    public Optional<Translation> findByDocumentIdAndLanguageIdWithData(Long documentId, Long languageId) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", documentId,
                "language_id", languageId));

        return first(
                jdbc.query(TranslationQuery.findByDocumentIdAndLanguageIdWithData(), params, dataMapper));
    }

    private Optional<Translation> first(List<Translation> list) {
        if (list.isEmpty()) {
            log.info("The translation not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public boolean exists(Long entityId) {
        return findById(entityId).isPresent();
    }

    @Override
    public boolean exists(Long documentId, Long languageId) {
        return findByDocumentIdAndLanguageId(documentId, languageId).isPresent();
    }

    @Override
    public Translation save(Translation translation) {
        checkInternalEntities(translation.getLanguage().getEntityId());

        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", translation.getDocument().getEntityId(),
                "language_id", translation.getLanguage().getEntityId(),
                "data", translation.getData()));


        int result = jdbc.update(TranslationQuery.save(), params);
        if (result != 1) {
            unpredictableError("Translation save error.");
        }

        Optional<Translation> opt =
                findByDocumentIdAndLanguageId(translation.getDocument().getEntityId(), translation.getLanguage().getEntityId());

        if (opt.isEmpty()) {
            unpredictableError("Translation save error during searching.");
        }

        return opt.get();
    }

    private void checkInternalEntities(Long languageId) {
        List<Language> rs =
                jdbc.query(LanguageQuery.findById(),
                        new MapSqlParameterSource("entity_id", languageId),
                        LanguageMapper.mapper);

        if (rs.isEmpty()) {
            throw new EntityNotFoundException("Language by id not found.");
        }
    }

    @Override
    public void deleteById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);

        int result = jdbc.update(TranslationQuery.deleteById(), params);
        if (result != 1) {
            unpredictableError("Translation delete error.");
        }
    }
}
