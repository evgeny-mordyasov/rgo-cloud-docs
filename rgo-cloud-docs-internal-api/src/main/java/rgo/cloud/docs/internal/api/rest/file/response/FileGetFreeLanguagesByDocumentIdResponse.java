package rgo.cloud.docs.internal.api.rest.file.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;
import rgo.cloud.docs.db.api.entity.Language;

import java.util.List;

@Builder
@Getter
@ToString
public class FileGetFreeLanguagesByDocumentIdResponse implements Response {
    private final Status status;
    private final List<Language> list;

    public static FileGetFreeLanguagesByDocumentIdResponse success(List<Language> list) {
        return FileGetFreeLanguagesByDocumentIdResponse.builder()
                .status(Status.success())
                .list(list)
                .build();
    }
}
