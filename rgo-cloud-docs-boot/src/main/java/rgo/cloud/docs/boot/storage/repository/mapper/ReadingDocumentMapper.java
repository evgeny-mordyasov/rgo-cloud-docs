package rgo.cloud.docs.boot.storage.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import rgo.cloud.docs.internal.api.storage.ReadingDocument;

public final class ReadingDocumentMapper {
    private ReadingDocumentMapper() {
    }

    public static final RowMapper<ReadingDocument> mapper = (rs, num) -> ReadingDocument.builder()
            .entityId(rs.getLong("ENTITY_ID"))
            .documentId(rs.getLong("DOCUMENT_ID"))
            .languageId(rs.getLong("LANGUAGE_ID"))
            .time(rs.getTimestamp("TIME").toLocalDateTime())
            .build();
}
