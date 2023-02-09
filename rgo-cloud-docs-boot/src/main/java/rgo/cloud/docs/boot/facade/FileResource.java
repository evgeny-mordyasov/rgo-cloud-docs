package rgo.cloud.docs.boot.facade;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder(setterPrefix = "with")
@Getter
@ToString
public class FileResource {
    private final byte[] data;
    private final String fullFileName;
}
