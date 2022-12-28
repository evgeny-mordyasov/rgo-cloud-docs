package rgo.cloud.docs.internal.api.rest.file.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorObjectId;

@AllArgsConstructor
@Getter
@ToString
public class FileGetByClassificationIdRequest implements Request {
    private final Long classificationId;

    @Override
    public void validate() {
        errorObjectId(classificationId, "classificationId");
    }
}
