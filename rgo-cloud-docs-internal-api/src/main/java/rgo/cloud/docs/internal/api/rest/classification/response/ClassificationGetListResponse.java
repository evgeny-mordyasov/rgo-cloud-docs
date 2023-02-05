package rgo.cloud.docs.internal.api.rest.classification.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;
import rgo.cloud.docs.db.api.entity.Classification;

import java.util.List;

@Builder
@Getter
@ToString
public class ClassificationGetListResponse implements Response {
    private final Status status;
    private final List<Classification> list;
    private final Integer total;

    public static ClassificationGetListResponse success(List<Classification> list) {
        return ClassificationGetListResponse.builder()
                .status(Status.success())
                .list(list)
                .total(list.size())
                .build();
    }
}
