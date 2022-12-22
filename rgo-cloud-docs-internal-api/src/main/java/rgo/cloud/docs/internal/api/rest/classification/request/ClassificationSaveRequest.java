package rgo.cloud.docs.internal.api.rest.classification.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorString;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class ClassificationSaveRequest implements Request {
    private final String name;

    @Override
    public void validate() {
        errorString(name, "name");
    }
}
