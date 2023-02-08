package rgo.cloud.docs.db.storage.repository.natural;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.db.storage.query.LanguageQuery;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.docs.db.api.repository.LanguageRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.common.api.util.ExceptionUtil.unpredictableError;
import static rgo.cloud.docs.db.storage.repository.natural.mapper.LanguageMapper.mapper;

@Slf4j
public class PostgresLanguageRepository implements LanguageRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresLanguageRepository(DbTxManager tx) {
        this.jdbc = tx.jdbc();
    }

    @Override
    public List<Language> findAll() {
        List<Language> languages = jdbc.query(LanguageQuery.findAll(), mapper);
        log.info("Size of languages: {}", languages.size());

        return languages;
    }

    @Override
    public Optional<Language> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(
                jdbc.query(LanguageQuery.findById(), params, mapper));
    }

    @Override
    public Optional<Language> findByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        return first(
                jdbc.query(LanguageQuery.findByName(), params, mapper));
    }

    private Optional<Language> first(List<Language> list) {
        if (list.isEmpty()) {
            log.info("The language not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public boolean exists(Long entityId) {
        return findById(entityId).isPresent();
    }

    @Override
    public Language save(Language language) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", language.getName());

        int result = jdbc.update(LanguageQuery.save(), params);
        if (result != 1) {
            unpredictableError("Language save error.");
        }

        Optional<Language> opt = findByName(language.getName());
        if (opt.isEmpty()) {
            unpredictableError("Language save error during searching.");
        }

        return opt.get();
    }

    @Override
    public Language update(Language language) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "entity_id", language.getEntityId(),
                "name", language.getName()));

        int result = jdbc.update(LanguageQuery.update(), params);
        if (result != 1) {
            unpredictableError("Language update error.");
        }

        Optional<Language> opt = findByName(language.getName());
        if (opt.isEmpty()) {
            unpredictableError("Language update error during searching.");
        }

        return opt.get();
    }
}
