package rgo.cloud.docs.internal.api.rest.file.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;
import rgo.cloud.docs.internal.api.facade.FileDto;

import java.util.List;

@Builder
@Getter
@ToString
public class FileGetListResponse implements Response {
    private final Status status;
    private final List<FileDto> list;
    private final Integer total;

    public static FileGetListResponse success(List<FileDto> list) {
        return FileGetListResponse.builder()
                .status(Status.success())
                .list(list)
                .total(list.size())
                .build();
    }
}
