package rgo.cloud.docs.internal.api.rest.file.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorObjectId;

@AllArgsConstructor
@Getter
@ToString
public class FileGetByDocumentIdRequest implements Request {
    private final Long documentId;

    @Override
    public void validate() {
        errorObjectId(documentId, "documentId");
    }
}