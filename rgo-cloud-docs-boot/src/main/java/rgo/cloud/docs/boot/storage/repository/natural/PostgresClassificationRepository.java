package rgo.cloud.docs.boot.storage.repository.natural;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.ClassificationQuery;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.common.api.util.ExceptionUtil.unpredictableError;
import static rgo.cloud.docs.boot.storage.repository.natural.mapper.ClassificationMapper.mapper;

@Slf4j
public class PostgresClassificationRepository implements ClassificationRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresClassificationRepository(DbTxManager tx) {
        this.jdbc = tx.jdbc();
    }

    @Override
    public List<Classification> findAll() {
        List<Classification> classifications = jdbc.query(ClassificationQuery.findAll(), mapper);
        log.info("Size of classifications: {}", classifications.size());

        return classifications;
    }

    @Override
    public Optional<Classification> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(
                jdbc.query(ClassificationQuery.findById(), params, mapper));
    }

    @Override
    public Optional<Classification> findByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        return first(
                jdbc.query(ClassificationQuery.findByName(), params, mapper));
    }

    private Optional<Classification> first(List<Classification> list) {
        if (list.isEmpty()) {
            log.info("The classification not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public boolean exists(Long entityId) {
        return findById(entityId).isPresent();
    }

    @Override
    public Classification save(Classification classification) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", classification.getName());

        int result = jdbc.update(ClassificationQuery.save(), params);
        if (result != 1) {
            unpredictableError("Classification save error.");
        }

        Optional<Classification> opt = findByName(classification.getName());
        if (opt.isEmpty()) {
            unpredictableError("Classification save error during searching.");
        }

        return opt.get();
    }

    @Override
    public Classification update(Classification classification) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "entity_id", classification.getEntityId(),
                "name", classification.getName()));

        int result = jdbc.update(ClassificationQuery.update(), params);
        if (result != 1) {
            unpredictableError("Classification update error.");
        }

        Optional<Classification> opt = findByName(classification.getName());
        if (opt.isEmpty()) {
            unpredictableError("Classification update error during searching.");
        }

        return opt.get();
    }

    @Override
    public void deleteById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);

        int result = jdbc.update(ClassificationQuery.deleteById(), params);
        if (result != 1) {
            unpredictableError("Classification delete error.");
        }
    }
}
