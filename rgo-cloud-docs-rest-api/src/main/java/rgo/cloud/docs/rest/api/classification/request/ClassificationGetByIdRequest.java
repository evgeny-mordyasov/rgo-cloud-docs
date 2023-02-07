package rgo.cloud.docs.rest.api.classification.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorEntityId;
import static rgo.cloud.common.api.util.ValidatorUtil.finish;

@AllArgsConstructor
@Getter
@ToString
public class ClassificationGetByIdRequest implements Request {
    private final Long entityId;

    @Override
    public void validate() {
        errorEntityId(entityId);
        finish();
    }
}
