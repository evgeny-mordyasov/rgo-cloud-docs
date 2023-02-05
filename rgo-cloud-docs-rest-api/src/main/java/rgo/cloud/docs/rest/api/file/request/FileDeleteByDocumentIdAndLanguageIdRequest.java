package rgo.cloud.docs.rest.api.file.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;

import static rgo.cloud.common.api.util.ValidatorUtil.errorObjectId;
import static rgo.cloud.common.api.util.ValidatorUtil.finish;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class FileDeleteByDocumentIdAndLanguageIdRequest implements Request {
    private final Long documentId;
    private final Long languageId;

    @Override
    public void validate() {
        errorObjectId(documentId, "documentId");
        errorObjectId(languageId, "languageId");
        finish();
    }
}