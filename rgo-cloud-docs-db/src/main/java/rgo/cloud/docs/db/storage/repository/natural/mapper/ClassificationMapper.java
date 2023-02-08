package rgo.cloud.docs.db.storage.repository.natural.mapper;

import org.springframework.jdbc.core.RowMapper;
import rgo.cloud.docs.db.api.entity.Classification;

public final class ClassificationMapper {
    private ClassificationMapper() {
    }

    public static final RowMapper<Classification> mapper = (rs, num) -> Classification.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .name(rs.getString("NAME"))
            .build();
}
