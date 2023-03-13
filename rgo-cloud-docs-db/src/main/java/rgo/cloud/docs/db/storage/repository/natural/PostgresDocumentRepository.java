package rgo.cloud.docs.db.storage.repository.natural;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.db.storage.query.ClassificationQuery;
import rgo.cloud.docs.db.storage.query.DocumentQuery;
import rgo.cloud.docs.db.storage.repository.natural.mapper.ClassificationMapper;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.entity.Document;
import rgo.cloud.docs.db.api.repository.DocumentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.common.api.util.ExceptionUtil.unpredictableError;
import static rgo.cloud.docs.db.storage.repository.natural.mapper.DocumentMapper.emptyMapper;
import static rgo.cloud.docs.db.storage.repository.natural.mapper.DocumentMapper.mapper;

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

    @Override
    public Optional<Document> findByFullName(String fullName) {
        MapSqlParameterSource params = new MapSqlParameterSource("full_name", fullName);
        return first(
                jdbc.query(DocumentQuery.findByFullName(), params, emptyMapper));
    }

    private Optional<Document> first(List<Document> list) {
        if (list.isEmpty()) {
            log.info("The document not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
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
            unpredictableError("Document save error.");
        }

        Optional<Document> opt = findById(key.longValue());
        if (opt.isEmpty()) {
            unpredictableError("Error saving the document when selecting by ID.");
        }

        return opt.get();
    }

    @Override
    public Document patchFileName(Document document) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "entity_id", document.getEntityId(),
                "full_name", document.getFullName(),
                "name", document.getName()));

        int result = jdbc.update(DocumentQuery.patchFileName(), params);
        if (result != 1) {
            unpredictableError("Error when changing the document file name.");
        }

        Optional<Document> opt = findById(document.getEntityId());
        if (opt.isEmpty()) {
            unpredictableError("Error when changing the document file name during searching.");
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
            unpredictableError("Document delete error.");
        }
    }
}
