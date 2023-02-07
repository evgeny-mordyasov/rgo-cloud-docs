package rgo.cloud.docs.db.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class TranslationKey {
    private final Long documentId;
    private final Long languageId;
}
