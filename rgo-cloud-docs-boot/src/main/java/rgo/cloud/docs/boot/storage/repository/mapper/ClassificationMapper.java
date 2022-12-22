package rgo.cloud.docs.boot.storage.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import rgo.cloud.docs.internal.api.storage.Classification;

public final class ClassificationMapper {
    private ClassificationMapper() {
    }

    public static final RowMapper<Classification> mapper = (rs, num) -> Classification.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .name(rs.getString("NAME"))
            .build();
}
