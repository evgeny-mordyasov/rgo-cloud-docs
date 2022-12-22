package rgo.cloud.docs.internal.api.rest.classification.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;
import rgo.cloud.docs.internal.api.storage.Classification;

@Builder
@Getter
@ToString
public class ClassificationModifyResponse implements Response {
    private final Status status;
    private final Classification object;

    public static ClassificationModifyResponse success(Classification classification) {
        return ClassificationModifyResponse.builder()
                .status(Status.success())
                .object(classification)
                .build();
    }
}
