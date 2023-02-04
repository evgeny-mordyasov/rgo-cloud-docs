package rgo.cloud.docs.internal.api.facade;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.docs.internal.api.storage.Language;

@Getter
@Builder(toBuilder = true)
@ToString
public class ResourceDto {
    private final Language language;
    private final String resource;
    private final long downloads;
}
