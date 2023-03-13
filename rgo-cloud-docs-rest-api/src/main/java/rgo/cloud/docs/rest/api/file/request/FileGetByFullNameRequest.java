package rgo.cloud.docs.rest.api.file.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorString;

@AllArgsConstructor
@Getter
@ToString
public class FileGetByFullNameRequest implements Request {
    private final String name;

    @Override
    public void validate() {
        errorString(name, "name");
    }
}