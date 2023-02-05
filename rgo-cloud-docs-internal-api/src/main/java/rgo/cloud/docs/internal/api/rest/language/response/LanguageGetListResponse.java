package rgo.cloud.docs.internal.api.rest.language.response;

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
public class LanguageGetListResponse implements Response {
    private final Status status;
    private final List<Language> list;
    private final Integer total;

    public static LanguageGetListResponse success(List<Language> list) {
        return LanguageGetListResponse.builder()
                .status(Status.success())
                .list(list)
                .total(list.size())
                .build();
    }
}
