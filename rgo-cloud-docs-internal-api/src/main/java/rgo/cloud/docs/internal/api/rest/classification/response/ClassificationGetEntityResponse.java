package rgo.cloud.docs.internal.api.rest.classification.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;
import rgo.cloud.docs.db.api.entity.Classification;

@Builder
@Getter
@ToString
public class ClassificationGetEntityResponse implements Response {
    private final Status status;
    private final Classification object;

    public static ClassificationGetEntityResponse success(Classification classification) {
        return ClassificationGetEntityResponse.builder()
                .status(Status.success())
                .object(classification)
                .build();
    }
}
