package rgo.cloud.docs.boot.storage.repository.natural.mapper;

import org.springframework.jdbc.core.RowMapper;
import rgo.cloud.docs.internal.api.storage.Language;

public final class LanguageMapper {
    private LanguageMapper() {
    }

    public static final RowMapper<Language> mapper = (rs, num) -> Language.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .name(rs.getString("NAME"))
            .build();
}
