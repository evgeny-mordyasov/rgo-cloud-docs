package rgo.cloud.docs.facade.api;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder(setterPrefix = "with")
@Getter
@ToString
public class FileResource {
    private final String fullFileName;
    private final byte[] data;
}
