package rgo.cloud.docs.boot.storage.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.query.LanguageQuery;
import rgo.cloud.docs.internal.api.storage.Language;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LanguageRepository {
    private final DbTxManager tx;
    private final NamedParameterJdbcTemplate jdbc;

    public LanguageRepository(DbTxManager tx) {
        this.tx = tx;
        this.jdbc = tx.jdbc();
    }

    public List<Language> findAll() {
        List<Language> languages = tx.tx(() -> jdbc.query(LanguageQuery.findAll(), mapper));
        log.info("Size of languages: {}", languages.size());

        return languages;
    }

    public Optional<Language> findById(Long entityId) {
        MapSqlParameterSource params = new MapSqlParameterSource("entity_id", entityId);
        return first(tx.tx(() ->
                jdbc.query(LanguageQuery.findById(), params, mapper)));
    }

    public Optional<Language> findByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        return first(tx.tx(() ->
                jdbc.query(LanguageQuery.findByName(), params, mapper)));
    }

    private Optional<Language> first(List<Language> list) {
        if (list.isEmpty()) {
            log.info("The language not found.");
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    public Language save(Language language) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", language.getName());

        return tx.tx(() -> {
            jdbc.update(LanguageQuery.save(), params);
            Optional<Language> opt = findByName(language.getName());

            if (opt.isEmpty()) {
                String errorMsg = "Language save error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            return opt.get();
        });
    }

    public Language update(Language language) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "entity_id", language.getEntityId(),
                "name", language.getName()));

        return tx.tx(() -> {
            jdbc.update(LanguageQuery.update(), params);
            Optional<Language> opt = findByName(language.getName());

            if (opt.isEmpty()) {
                String errorMsg = "Language update error.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            return opt.get();
        });
    }

    private static final RowMapper<Language> mapper = (rs, num) -> Language.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .name(rs.getString("NAME"))
            .build();
}
