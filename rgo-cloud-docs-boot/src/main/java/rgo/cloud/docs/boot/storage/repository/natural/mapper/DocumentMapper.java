package rgo.cloud.docs.boot.storage.repository.natural.mapper;

import org.springframework.jdbc.core.RowMapper;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.entity.Document;

public final class DocumentMapper {
    private DocumentMapper() {
    }

    public static final RowMapper<Document> mapper = (rs, num) -> Document.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .fullName(rs.getString("FULL_NAME"))
            .name(rs.getString("NAME"))
            .extension(rs.getString("EXTENSION"))
            .classification(Classification.builder()
                    .entityId(rs.getLong("CLASSIFICATION_ID"))
                    .name(rs.getString("CLASSIFICATION_NAME"))
                    .build())
            .build();
}
