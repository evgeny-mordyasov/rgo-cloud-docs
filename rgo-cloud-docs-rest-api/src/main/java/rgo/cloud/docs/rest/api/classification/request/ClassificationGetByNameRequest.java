package rgo.cloud.docs.rest.api.classification.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorString;
import static rgo.cloud.common.api.util.ValidatorUtil.finish;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class ClassificationGetByNameRequest implements Request {
    private final String name;

    @Override
    public void validate() {
        errorString(name, "name");
        finish();
    }
}