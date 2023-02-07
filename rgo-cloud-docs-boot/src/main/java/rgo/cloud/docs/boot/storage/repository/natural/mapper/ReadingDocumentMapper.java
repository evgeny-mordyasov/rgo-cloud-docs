package rgo.cloud.docs.boot.storage.repository.natural.mapper;

import org.springframework.jdbc.core.RowMapper;
import rgo.cloud.docs.db.api.entity.ReadingDocument;
import rgo.cloud.docs.db.api.entity.TranslationKey;

public final class ReadingDocumentMapper {
    private ReadingDocumentMapper() {
    }

    public static final RowMapper<ReadingDocument> mapper = (rs, num) -> ReadingDocument.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .key(TranslationKey.builder()
                    .documentId(rs.getLong("DOCUMENT_ID"))
                    .languageId(rs.getLong("LANGUAGE_ID"))
                    .build())
            .time(rs.getTimestamp("TIME").toLocalDateTime())
            .build();
}
