package rgo.cloud.docs.internal.api.facade;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.docs.internal.api.storage.Document;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@ToString
public class FileDto {
    private final Document document;
    private final List<ResourceDto> resources;
}
