package rgo.cloud.docs.db.api.entity;

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
    private final TranslationKey key;
    private final LocalDateTime time;
}
