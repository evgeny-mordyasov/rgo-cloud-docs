package rgo.cloud.docs.boot.storage.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;
import rgo.cloud.docs.internal.api.storage.Language;

public final class DocumentLanguageMapper {
    private DocumentLanguageMapper() {
    }

    public static final RowMapper<DocumentLanguage> lazyMapper =
            (rs, num) -> DocumentLanguage.builder()
                    .entityId(rs.getLong("ENTITY_ID"))
                    .document(Document.builder()
                            .entityId(rs.getLong("DOCUMENT_ID"))
                            .fullName(rs.getString("DOCUMENT_FULL_NAME"))
                            .name(rs.getString("DOCUMENT_NAME"))
                            .extension(rs.getString("DOCUMENT_EXTENSION"))
                            .classification(Classification.builder()
                                    .entityId(rs.getLong("DOCUMENT_CLASSIFICATION_ID"))
                                    .name(rs.getString("DOCUMENT_CLASSIFICATION_NAME"))
                                    .build())
                            .build())
                    .language(Language.builder()
                            .entityId(rs.getLong("LANGUAGE_ID"))
                            .name(rs.getString("LANGUAGE_NAME"))
                            .build())
                    .build();

    public static final RowMapper<DocumentLanguage> emptyMapper =
            (rs, num) -> DocumentLanguage.builder()
                    .entityId(rs.getLong("ENTITY_ID"))
                    .build();

    public static final RowMapper<DocumentLanguage> dataMapper =
            (rs, num) -> DocumentLanguage.builder()
                    .data(rs.getBytes("DATA"))
                    .document(Document.builder()
                            .fullName(rs.getString("DOCUMENT_FULL_NAME"))
                            .build())
                    .build();
}
