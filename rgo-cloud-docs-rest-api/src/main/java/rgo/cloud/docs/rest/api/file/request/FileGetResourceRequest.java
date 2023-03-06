package rgo.cloud.docs.rest.api.file.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Request;
import rgo.cloud.docs.db.api.entity.TranslationKey;

import static rgo.cloud.common.api.util.ValidatorUtil.errorObjectId;

@AllArgsConstructor
@Getter
@ToString
public class FileGetResourceRequest implements Request {
    private final TranslationKey key;

    @Override
    public void validate() {
        errorObjectId(key.getDocumentId(), "documentId");
        errorObjectId(key.getLanguageId(), "languageId");
    }
}
