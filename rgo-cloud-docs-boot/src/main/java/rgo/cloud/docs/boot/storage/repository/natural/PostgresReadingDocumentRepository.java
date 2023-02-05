package rgo.cloud.docs.boot.storage.repository.natural;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.ReadingDocumentQuery;
import rgo.cloud.docs.db.api.entity.ReadingDocument;
import rgo.cloud.docs.db.api.repository.ReadingDocumentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.docs.boot.storage.repository.natural.mapper.ReadingDocumentMapper.mapper;

@Slf4j
public class PostgresReadingDocumentRepository implements ReadingDocumentRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresReadingDocumentRepository(DbTxManager tx) {
        this.jdbc = tx.jdbc();
    }

    @Override
    public ReadingDocument save(ReadingDocument rd) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "document_id", rd.getDocumentId(),
                "language_id", rd.getLanguageId()));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbc.update(ReadingDocumentQuery.save(), params, keyHolder, new String[]{"entity_id"});
        Number key = keyHolder.getKey();

        if (result != 1 || key == null) {
            String errorMsg = "ReadingDocument save error.";
            log.error(errorMsg);
            throw new UnpredictableException(errorMsg);
        }

        Optional<ReadingDocument> opt = findById(key.longValue());
        if (opt.isEmpty()) {
            String errorMsg = "Error saving the readingDocument when selecting by ID.";
            log.error(errorMsg);
            throw new UnpredictableException(errorMsg);
        }

        return opt.get();
    }

    private Optional<ReadingDocument> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(
                jdbc.query(ReadingDocumentQuery.findById(), params, mapper));
    }

    private Optional<ReadingDocument> first(List<ReadingDocument> list) {
        if (list.isEmpty()) {
            log.info("The readingDocument not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }
}
