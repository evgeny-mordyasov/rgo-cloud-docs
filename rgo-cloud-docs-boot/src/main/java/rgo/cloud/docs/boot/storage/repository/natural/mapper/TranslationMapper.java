package rgo.cloud.docs.boot.storage.repository.natural.mapper;

import org.springframework.jdbc.core.RowMapper;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.entity.Document;
import rgo.cloud.docs.db.api.entity.Translation;
import rgo.cloud.docs.db.api.entity.Language;

public final class TranslationMapper {
    private TranslationMapper() {
    }

    public static final RowMapper<Translation> lazyMapper =
            (rs, num) -> Translation.builder()
                    .entityId(rs.getLong("TR_ENTITY_ID"))
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
                    .downloads(rs.getLong("DOWNLOADS"))
                    .build();

    public static final RowMapper<Translation> emptyMapper =
            (rs, num) -> Translation.builder()
                    .entityId(rs.getLong("ENTITY_ID"))
                    .build();

    public static final RowMapper<Translation> dataMapper =
            (rs, num) -> Translation.builder()
                    .data(rs.getBytes("DATA"))
                    .document(Document.builder()
                            .fullName(rs.getString("DOCUMENT_FULL_NAME"))
                            .build())
                    .build();
}
