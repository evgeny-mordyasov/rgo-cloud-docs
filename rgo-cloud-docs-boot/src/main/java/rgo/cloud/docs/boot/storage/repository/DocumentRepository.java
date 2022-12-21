package rgo.cloud.docs.boot.storage.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.DocumentQuery;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class DocumentRepository {
    private final DbTxManager tx;
    private final NamedParameterJdbcTemplate jdbc;

    public DocumentRepository(DbTxManager tx) {
        this.tx = tx;
        this.jdbc = tx.jdbc();
    }

    public Optional<Document> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(tx.tx(() ->
                jdbc.query(DocumentQuery.findByIdAndFetchClassification(), params, mapper)));
    }

    private Optional<Document> first(List<Document> list) {
        if (list.isEmpty()) {
            log.info("The document not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    public boolean exists(Long entityId) {
        return findById(entityId).isPresent();
    }

    public Document save(Document document) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "full_name", document.getFullName(),
                "name", document.getName(),
                "extension", document.getExtension(),
                "classification_id", document.getClassification().getEntityId()));

        return tx.tx(() -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int result = jdbc.update(DocumentQuery.save(), params, keyHolder);
            Number key = keyHolder.getKey();

            if (result == 0 || key == null) {
                String errorMsg = "Document save error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            Optional<Document> opt = findById(key.longValue());
            if (opt.isEmpty()) {
                String errorMsg = "Error saving document during classification selection.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            return opt.get();
        });
    }

    public void deleteById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);

        tx.tx(() -> {
            int result = jdbc.update(DocumentQuery.deleteById(), params);

            if (result == 0) {
                String errorMsg = "Document delete error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }
        });
    }

    private static final RowMapper<Document> mapper = (rs, num) -> Document.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .fullName(rs.getString("FULL_NAME"))
            .name(rs.getString("NAME"))
            .extension(rs.getString("EXTENSION"))
            .classification(Classification.builder()
                    .entityId(rs.getLong("CLASSIFICATION_ID"))
                    .name(rs.getString("CLASSIFICATION_NAME"))
                    .build())
            .build();
}
