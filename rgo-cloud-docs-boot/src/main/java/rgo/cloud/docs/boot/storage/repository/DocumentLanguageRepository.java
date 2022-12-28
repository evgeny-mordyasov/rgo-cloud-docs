package rgo.cloud.docs.boot.storage.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.DocumentLanguageQuery;
import rgo.cloud.docs.boot.storage.query.DocumentQuery;
import rgo.cloud.docs.boot.storage.query.LanguageQuery;
import rgo.cloud.docs.boot.storage.repository.mapper.DocumentMapper;
import rgo.cloud.docs.boot.storage.repository.mapper.LanguageMapper;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;
import rgo.cloud.docs.internal.api.storage.Language;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.docs.boot.storage.repository.mapper.DocumentLanguageMapper.emptyMapper;
import static rgo.cloud.docs.boot.storage.repository.mapper.DocumentLanguageMapper.lazyMapper;
import static rgo.cloud.docs.boot.storage.repository.mapper.DocumentLanguageMapper.fullMapper;

@Slf4j
public class DocumentLanguageRepository {
    private final DbTxManager tx;
    private final NamedParameterJdbcTemplate jdbc;

    public DocumentLanguageRepository(DbTxManager tx) {
        this.tx = tx;
        this.jdbc = tx.jdbc();
    }

    public List<DocumentLanguage> findAll() {
        List<DocumentLanguage> documentLanguages = tx.tx(() ->
                jdbc.query(DocumentLanguageQuery.findAll(), lazyMapper));
        log.info("Size of documentLanguages: {}", documentLanguages.size());

        return documentLanguages;
    }

    public List<DocumentLanguage> findByDocumentId(Long documentId) {
        MapSqlParameterSource params = new MapSqlParameterSource("document_id", documentId);
        List<DocumentLanguage> documentLanguages = tx.tx(() ->
                jdbc.query(DocumentLanguageQuery.findByDocumentId(), params, lazyMapper));
        log.info("Size of documentLanguages by documentId='{}': {}", documentId, documentLanguages.size());

        return documentLanguages;
    }

    public List<DocumentLanguage> findByClassificationId(Long classificationId) {
        MapSqlParameterSource params = new MapSqlParameterSource("classification_id", classificationId);
        List<DocumentLanguage> documentLanguages = tx.tx(() ->
                jdbc.query(DocumentLanguageQuery.findByClassificationId(), params, lazyMapper));
        log.info("Size of documentLanguages by classificationId='{}': {}", classificationId, documentLanguages.size());

        return documentLanguages;
    }

    private Optional<DocumentLanguage> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(tx.tx(() ->
                jdbc.query(DocumentLanguageQuery.findById(), params, emptyMapper)));
    }

    public Optional<DocumentLanguage> findByDocumentIdAndLanguageId(Long documentId, Long languageId) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", documentId,
                "language_id", languageId));

        return first(tx.tx(() ->
                jdbc.query(DocumentLanguageQuery.findByDocumentIdAndLanguageId(), params, lazyMapper)));
    }

    public Optional<DocumentLanguage> findByDocumentIdAndLanguageIdWithData(Long documentId, Long languageId) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", documentId,
                "language_id", languageId));

        return first(tx.tx(() ->
                jdbc.query(DocumentLanguageQuery.findByDocumentIdAndLanguageIdWithData(), params, fullMapper)));
    }

    private Optional<DocumentLanguage> first(List<DocumentLanguage> list) {
        if (list.isEmpty()) {
            log.info("The documentLanguage not found.");
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

    public DocumentLanguage save(DocumentLanguage documentLanguage) {
        checkInternalEntities(documentLanguage);

        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", documentLanguage.getDocument().getEntityId(),
                "language_id", documentLanguage.getLanguage().getEntityId(),
                "data", documentLanguage.getData()));

        return tx.tx(() -> {
            jdbc.update(DocumentLanguageQuery.save(), params);
            Optional<DocumentLanguage> opt =
                    findByDocumentIdAndLanguageId(documentLanguage.getDocument().getEntityId(), documentLanguage.getLanguage().getEntityId());

            if (opt.isEmpty()) {
                String errorMsg = "DocumentLanguage save error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            return opt.get();
        });
    }

    private void checkInternalEntities(DocumentLanguage dl) {
        checkLanguage(dl.getLanguage().getEntityId());
        checkDocument(dl.getDocument().getEntityId());
    }

    private void checkLanguage(Long languageId) {
        List<Language> rs = tx.tx(() ->
                jdbc.query(LanguageQuery.findById(),
                        new MapSqlParameterSource("entity_id", languageId),
                        LanguageMapper.mapper));

        if (rs.isEmpty()) {
            throw new EntityNotFoundException("Language by id not found.");
        }
    }

    private void checkDocument(Long documentId) {
        List<Document> rs = tx.tx(() ->
                jdbc.query(DocumentQuery.findByIdAndFetchClassification(),
                        new MapSqlParameterSource("entity_id", documentId),
                        DocumentMapper.mapper));

        if (rs.isEmpty()) {
            throw new EntityNotFoundException("Document by id not found.");
        }
    }

    public void deleteById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);

        tx.tx(() -> {
            int result = jdbc.update(DocumentLanguageQuery.deleteById(), params);

            if (result == 0) {
                String errorMsg = "DocumentLanguage delete error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }
        });
    }
}
