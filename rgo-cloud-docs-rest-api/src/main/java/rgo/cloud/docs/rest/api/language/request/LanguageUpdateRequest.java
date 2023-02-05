package rgo.cloud.docs.rest.api.language.request;

import lombok.*;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class LanguageUpdateRequest implements Request {
    private final Long entityId;
    private final String name;

    @Override
    public void validate() {
        errorEntityId(entityId);
        errorString(name, "name");
        finish();
    }
}
