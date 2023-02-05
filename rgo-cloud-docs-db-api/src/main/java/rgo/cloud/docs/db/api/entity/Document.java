package rgo.cloud.docs.db.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Document {
    private final Long entityId;
    private final String fullName;
    private final String name;
    private final String extension;
    private final Classification classification;
}
