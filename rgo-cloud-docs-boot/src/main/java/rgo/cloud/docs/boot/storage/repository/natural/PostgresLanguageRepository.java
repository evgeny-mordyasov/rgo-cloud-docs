package rgo.cloud.docs.boot.storage.repository.natural;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.LanguageQuery;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.docs.db.api.repository.LanguageRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rgo.cloud.docs.boot.storage.repository.natural.mapper.LanguageMapper.mapper;

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

        jdbc.update(LanguageQuery.save(), params);
        Optional<Language> opt = findByName(language.getName());

        if (opt.isEmpty()) {
            String errorMsg = "Language save error.";
            log.error(errorMsg);
            throw new UnpredictableException(errorMsg);
        }

        return opt.get();
    }

    @Override
    public Language update(Language language) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "entity_id", language.getEntityId(),
                "name", language.getName()));

        jdbc.update(LanguageQuery.update(), params);
        Optional<Language> opt = findByName(language.getName());

        if (opt.isEmpty()) {
            String errorMsg = "Language update error.";
            log.error(errorMsg);
            throw new UnpredictableException(errorMsg);
        }

        return opt.get();
    }
}
