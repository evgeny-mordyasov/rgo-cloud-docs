package rgo.cloud.docs.internal.api.storage;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class DocumentLanguage {
    private final Long entityId;
    private final Document document;
    private final Language language;
    private final byte[] data;
}
