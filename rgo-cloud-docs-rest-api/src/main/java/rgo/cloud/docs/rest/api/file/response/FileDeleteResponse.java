package rgo.cloud.docs.rest.api.file.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;

@Builder
@Getter
@ToString
public class FileDeleteResponse implements Response {
    private final Status status;

    public static FileDeleteResponse success() {
        return FileDeleteResponse.builder()
                .status(Status.success())
                .build();
    }
}
