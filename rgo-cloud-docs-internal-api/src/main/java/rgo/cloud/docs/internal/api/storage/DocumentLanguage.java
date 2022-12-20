package rgo.cloud.docs.internal.api.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class DocumentLanguage {
    private final Long entityId;
    private final Document document;
    private final Language language;
    private final byte[] data;
}
