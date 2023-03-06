package rgo.cloud.docs.rest.api.classification.request;

import lombok.*;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorEntityId;
import static rgo.cloud.common.api.util.ValidatorUtil.errorString;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class ClassificationUpdateRequest implements Request {
    private final Long entityId;
    private final String name;

    @Override
    public void validate() {
        errorEntityId(entityId);
        errorString(name, "name");
    }
}
