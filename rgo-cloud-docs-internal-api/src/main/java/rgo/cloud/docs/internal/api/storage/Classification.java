package rgo.cloud.docs.internal.api.storage;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Classification {
    private final Long entityId;
    private final String name;
}
