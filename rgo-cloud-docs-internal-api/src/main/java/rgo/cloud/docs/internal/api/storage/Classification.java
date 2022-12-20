package rgo.cloud.docs.internal.api.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class Classification {
    private final Long entityId;
    private final String name;
}
