package rgo.cloud.docs.facade.api;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MultipartFileDto {
    private final String fullFileName;
    private final String fileName;
    private final String extension;

    @ToString.Exclude
    private final byte[] data;

    private final boolean isEmpty;
    private final long size;
}
