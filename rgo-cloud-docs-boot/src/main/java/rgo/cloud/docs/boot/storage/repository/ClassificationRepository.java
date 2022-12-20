package rgo.cloud.docs.boot.storage.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.ClassificationQuery;
import rgo.cloud.docs.internal.api.storage.Classification;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// TODO: validate ID in the service layer

@Slf4j
public class ClassificationRepository {
    private final DbTxManager tx;
    private final NamedParameterJdbcTemplate jdbc;

    public ClassificationRepository(DbTxManager tx) {
        this.tx = tx;
        this.jdbc = tx.jdbc();
    }

    public List<Classification> findAll() {
        List<Classification> classifications = tx.tx(() -> jdbc.query(ClassificationQuery.findAll(), mapper));
        log.info("Size of classifications: {}", classifications.size());

        return classifications;
    }

    public Optional<Classification> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(tx.tx(() ->
                jdbc.query(ClassificationQuery.findById(), params, mapper)));
    }

    public Optional<Classification> findByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        return first(tx.tx(() ->
                jdbc.query(ClassificationQuery.findByName(), params, mapper)));
    }

    private Optional<Classification> first(List<Classification> list) {
        if (list.isEmpty()) {
            log.info("The classification not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    public Classification save(Classification classification) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", classification.getName());

        return tx.tx(() -> {
            jdbc.update(ClassificationQuery.save(), params);
            Optional<Classification> opt = findByName(classification.getName());

            if (opt.isEmpty()) {
                String errorMsg = "Classification save error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            return opt.get();
        });
    }

    public Classification update(Classification classification) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "entity_id", classification.getEntityId(),
                "name", classification.getName()));

        return tx.tx(() -> {
            jdbc.update(ClassificationQuery.update(), params);
            Optional<Classification> opt = findByName(classification.getName());

            if (opt.isEmpty()) {
                String errorMsg = "Classification update error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            return opt.get();
        });
    }

    public void deleteById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);

        tx.tx(() -> {
            int result = jdbc.update(ClassificationQuery.deleteById(), params);

            if (result == 0) {
                String errorMsg = "Classification delete error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }
        });
    }

    private static final RowMapper<Classification> mapper = (rs, num) -> Classification.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .name(rs.getString("NAME"))
            .build();
}
