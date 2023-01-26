package rgo.cloud.docs.internal.api.rest.language.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorEntityId;
import static rgo.cloud.common.api.util.ValidatorUtil.finish;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class LanguageGetByIdRequest implements Request {
    private final Long entityId;

    @Override
    public void validate() {
        errorEntityId(entityId);
        finish();
    }
}
