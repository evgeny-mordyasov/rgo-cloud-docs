package rgo.cloud.docs.boot.storage.repository.natural;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.ClassificationQuery;
import rgo.cloud.docs.boot.storage.query.DocumentQuery;
import rgo.cloud.docs.boot.storage.repository.natural.mapper.ClassificationMapper;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.db.api.repository.DocumentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.docs.boot.storage.repository.natural.mapper.DocumentMapper.mapper;

@Slf4j
public class PostgresDocumentRepository implements DocumentRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresDocumentRepository(DbTxManager tx) {
        this.jdbc = tx.jdbc();
    }

    @Override
    public List<Document> findAll() {
        List<Document> documents = jdbc.query(DocumentQuery.findAll(), mapper);
        log.info("Size of documents: {}", documents.size());

        return documents;
    }

    @Override
    public Optional<Document> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(
                jdbc.query(DocumentQuery.findById(), params, mapper));
    }

    private Optional<Document> first(List<Document> list) {
        if (list.isEmpty()) {
            log.info("The document not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public boolean exists(Long entityId) {
        return findById(entityId).isPresent();
    }

    @Override
    public Document save(Document document) {
        checkInternalEntity(document.getClassification().getEntityId());

        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "full_name", document.getFullName(),
                "name", document.getName(),
                "extension", document.getExtension(),
                "classification_id", document.getClassification().getEntityId()));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbc.update(DocumentQuery.save(), params, keyHolder, new String[]{"entity_id"});
        Number key = keyHolder.getKey();

        if (result != 1 || key == null) {
            String errorMsg = "Document save error.";
            log.error(errorMsg);
            throw new UnpredictableException(errorMsg);
        }

        Optional<Document> opt = findById(key.longValue());
        if (opt.isEmpty()) {
            String errorMsg = "Error saving the document when selecting by ID.";
            log.error(errorMsg);
            throw new UnpredictableException(errorMsg);
        }

        return opt.get();
    }

    private void checkInternalEntity(Long classificationId) {
        List<Classification> rs =
                jdbc.query(ClassificationQuery.findById(),
                        new MapSqlParameterSource("entity_id", classificationId),
                        ClassificationMapper.mapper);

        if (rs.isEmpty()) {
            throw new EntityNotFoundException("Classification by id not found.");
        }
    }

    @Override
    public void deleteById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);

        int result = jdbc.update(DocumentQuery.deleteById(), params);
        if (result != 1) {
            String errorMsg = "Document delete error.";
            log.error(errorMsg);
            throw new UnpredictableException(errorMsg);
        }
    }
}