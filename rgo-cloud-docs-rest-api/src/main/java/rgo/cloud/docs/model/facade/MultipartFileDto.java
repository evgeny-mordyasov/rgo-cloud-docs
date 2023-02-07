package rgo.cloud.docs.model.facade;

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
    private final byte[] data;
    private final boolean isEmpty;
    private final long size;
}
