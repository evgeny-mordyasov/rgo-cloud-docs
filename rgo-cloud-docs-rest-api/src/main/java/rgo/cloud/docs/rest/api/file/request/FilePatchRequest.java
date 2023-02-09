package rgo.cloud.docs.rest.api.file.request;

import lombok.*;
import rgo.cloud.common.api.rest.Request;
import rgo.cloud.docs.db.api.entity.TranslationKey;
import rgo.cloud.docs.facade.api.MultipartFileDto;
import rgo.cloud.docs.rest.api.file.FileExtension;

import static rgo.cloud.common.api.util.ValidatorUtil.*;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class FilePatchRequest implements Request {
    private final MultipartFileDto file;
    private final TranslationKey key;

    @Override
    public void validate() {
        errorObjectId(key.getDocumentId(), "documentId");
        errorObjectId(key.getLanguageId(), "languageId");
        errorTrue(file.isEmpty(), "The file is missing.");
        errorString(file.getFileName(), "fileName");
        errorFalse(FileExtension.isFromWhitelist(file.getExtension()), "The file extension not supported.");
        errorFalse(file.getSize() <= 1024 * 1024 * 5, "The file size exceeds 5 MB.");
        finish();
    }
}

