package rgo.cloud.docs.internal.api.storage;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class ReadingDocument {
    private final Long entityId;
    private final Long documentId;
    private final Long languageId;
    private final LocalDateTime time;
}
