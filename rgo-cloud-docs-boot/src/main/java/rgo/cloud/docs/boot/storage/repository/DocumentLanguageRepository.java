package rgo.cloud.docs.boot.storage.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.DocumentLanguageQuery;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;
import rgo.cloud.docs.internal.api.storage.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// TODO: validate ID in the service layer
// TODO: refactoring *withData

@Slf4j
public class DocumentLanguageRepository {
    private final DbTxManager tx;
    private final NamedParameterJdbcTemplate jdbc;

    public DocumentLanguageRepository(DbTxManager tx) {
        this.tx = tx;
        this.jdbc = tx.jdbc();
    }

    public List<DocumentLanguage> findAll() {
        List<DocumentLanguage> documentLanguages = tx.tx(() -> jdbc.query(DocumentLanguageQuery.findAll(), lazyMapper));
        log.info("Size of documentLanguages: {}", documentLanguages.size());

        return documentLanguages;
    }

    public List<DocumentLanguage> findByDocumentId(Long documentId) {
        MapSqlParameterSource params = new MapSqlParameterSource("document_id", documentId);
        List<DocumentLanguage> documentLanguages = tx.tx(() -> jdbc.query(DocumentLanguageQuery.findByDocumentId(), params, lazyMapper));
        log.info("Size of documentLanguages by documentId='{}': {}", documentId, documentLanguages.size());

        return documentLanguages;
    }

    public List<DocumentLanguage> findByClassificationId(Long classificationId) {
        MapSqlParameterSource params = new MapSqlParameterSource("classification_id", classificationId);
        List<DocumentLanguage> documentLanguages = tx.tx(() -> jdbc.query(DocumentLanguageQuery.findByClassificationId(), params, lazyMapper));
        log.info("Size of documentLanguages by classificationId='{}': {}", classificationId, documentLanguages.size());

        return documentLanguages;
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
                jdbc.query(DocumentLanguageQuery.findByDocumentIdAndLanguageIdWithData(), params, mapper)));
    }

    private Optional<DocumentLanguage> first(List<DocumentLanguage> list) {
        if (list.isEmpty()) {
            log.info("The documentLanguage not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    public DocumentLanguage save(DocumentLanguage documentLanguage) {
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

    private static final RowMapper<DocumentLanguage> lazyMapper = (rs, num) -> map(rs).build();

    private static final RowMapper<DocumentLanguage> mapper = (rs, num) -> map(rs)
            .data(rs.getBytes("DATA"))
            .build();

    private static DocumentLanguage.DocumentLanguageBuilder map(ResultSet rs) throws SQLException {
        return DocumentLanguage.builder()
                .entityId(rs.getLong("ENTITY_ID"))
                .document(Document.builder()
                        .entityId(rs.getLong("DOCUMENT_ID"))
                        .fullName(rs.getString("DOCUMENT_FULL_NAME"))
                        .name(rs.getString("DOCUMENT_NAME"))
                        .extension(rs.getString("DOCUMENT_EXTENSION"))
                        .classification(Classification.builder()
                                .entityId(rs.getLong("DOCUMENT_CLASSIFICATION_ID"))
                                .name(rs.getString("DOCUMENT_CLASSIFICATION_NAME"))
                                .build())
                        .build())
                .language(Language.builder()
                        .entityId(rs.getLong("LANGUAGE_ID"))
                        .name(rs.getString("LANGUAGE_NAME"))
                        .build());
    }
}
