package rgo.cloud.docs.internal.api.rest.classification.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorEntityId;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class ClassificationGetByIdRequest implements Request {
    private final Long entityId;

    @Override
    public void validate() {
        errorEntityId(entityId);
    }
}
