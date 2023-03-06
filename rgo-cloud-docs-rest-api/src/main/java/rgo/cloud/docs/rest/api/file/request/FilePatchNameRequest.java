package rgo.cloud.docs.rest.api.file.request;

import lombok.*;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorObjectId;
import static rgo.cloud.common.api.util.ValidatorUtil.errorString;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class FilePatchNameRequest implements Request {
    private static final long serialVersionUID = 1L;

    private final Long documentId;
    private final String fileName;

    @Override
    public void validate() {
        errorObjectId(documentId, "documentId");
        errorString(fileName, "fileName");
    }
}
