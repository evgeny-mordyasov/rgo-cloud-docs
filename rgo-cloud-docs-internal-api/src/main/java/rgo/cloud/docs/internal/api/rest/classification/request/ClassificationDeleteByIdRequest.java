package rgo.cloud.docs.internal.api.rest.classification.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorEntityId;

@AllArgsConstructor
@Getter
@ToString
public class ClassificationDeleteByIdRequest implements Request {
    private final Long entityId;

    @Override
    public void validate() {
        errorEntityId(entityId);
    }
}
