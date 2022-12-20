package rgo.cloud.docs.internal.api.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class Document {
    private final Long entityId;
    private final String fullName;
    private final String name;
    private final String extension;
    private final Classification classification;
}
