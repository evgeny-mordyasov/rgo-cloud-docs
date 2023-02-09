package rgo.cloud.docs.rest.api.file.request;

import lombok.*;
import rgo.cloud.common.api.rest.Request;
import rgo.cloud.docs.facade.api.MultipartFileDto;
import rgo.cloud.docs.rest.api.file.FileExtension;

import static rgo.cloud.common.api.util.ValidatorUtil.*;

@Builder
@AllArgsConstructor

@Getter
@ToString
public class FileSaveRequest implements Request {
    private static final long serialVersionUID = 1L;

    private final MultipartFileDto file;
    private final Long classificationId;
    private final Long languageId;

    @Override
    public void validate() {
        errorObjectId(classificationId, "classificationId");
        errorObjectId(languageId, "languageId");
        errorTrue(file.isEmpty(), "The file is missing.");
        errorString(file.getFileName(), "fileName");
        errorFalse(FileExtension.isFromWhitelist(file.getExtension()), "The file extension not supported.");
        errorFalse(file.getSize() <= 1024 * 1024 * 5, "The file size exceeds 5 MB.");
        finish();
    }
}
