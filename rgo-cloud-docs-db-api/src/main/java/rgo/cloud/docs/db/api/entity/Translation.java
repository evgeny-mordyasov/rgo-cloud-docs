package rgo.cloud.docs.db.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static rgo.cloud.common.api.util.ExceptionUtil.unpredictableError;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Translation {
    private final Long entityId;
    private final Document document;
    private final Language language;
    private final long downloads;
    private final byte[] data;

    public TranslationKey key() {
        if (document == null || language == null) {
            unpredictableError("The document or language is null.");
        }

        return TranslationKey.builder()
                .documentId(document.getEntityId())
                .languageId(language.getEntityId())
                .build();
    }
}
