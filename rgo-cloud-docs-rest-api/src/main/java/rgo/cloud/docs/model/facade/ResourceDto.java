package rgo.cloud.docs.model.facade;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.docs.db.api.entity.Language;

@Getter
@Builder(toBuilder = true)
@ToString
public class ResourceDto {
    private final Language language;
    private final String resource;
    private final long downloads;
}
