package rgo.cloud.docs.internal.api.rest.classification.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;

@Builder
@Getter
@ToString
public class ClassificationDeleteResponse implements Response {
    private final Status status;

    public static ClassificationDeleteResponse success() {
        return ClassificationDeleteResponse.builder()
                .status(Status.success())
                .build();
    }
}
