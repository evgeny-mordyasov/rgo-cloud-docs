package rgo.cloud.docs.internal.api.rest.file.request;

import lombok.*;
import rgo.cloud.common.api.rest.Request;
import rgo.cloud.docs.internal.api.facade.MultipartFileDto;

import static rgo.cloud.common.api.util.ValidatorUtil.*;
import static rgo.cloud.docs.internal.api.constant.FileExtension.isFromWhitelist;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class FilePatchRequest implements Request {
    private final MultipartFileDto file;
    private final Long documentId;
    private final Long languageId;

    @Override
    public void validate() {
        errorObjectId(documentId, "documentId");
        errorObjectId(languageId, "languageId");
        errorTrue(file.isEmpty(), "The file is missing.");
        errorString(file.getFullFileName(), "fileName");
        errorFalse(isFromWhitelist(file.getExtension()), "The file extension not supported.");
        errorFalse(file.getSize() <= 1024 * 1024 * 5, "The file size exceeds 5 MB.");
    }
}

