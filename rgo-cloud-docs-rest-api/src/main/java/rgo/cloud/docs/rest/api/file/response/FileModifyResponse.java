package rgo.cloud.docs.rest.api.file.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;
import rgo.cloud.docs.rest.api.facade.FileDto;

@Builder
@Getter
@ToString
public class FileModifyResponse implements Response {
    private final Status status;
    private final FileDto object;

    public static FileModifyResponse success(FileDto file) {
        return FileModifyResponse.builder()
                .status(Status.success())
                .object(file)
                .build();
    }
}
