package rgo.cloud.docs.internal.api.facade;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.docs.db.api.entity.Document;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@ToString
public class FileDto {
    private final Document document;
    private final List<ResourceDto> resources;
    private final long downloads;
}
