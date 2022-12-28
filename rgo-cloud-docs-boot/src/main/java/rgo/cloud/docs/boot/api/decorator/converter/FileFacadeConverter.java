package rgo.cloud.docs.boot.api.decorator.converter;

import rgo.cloud.docs.internal.api.rest.file.request.FilePatchRequest;
import rgo.cloud.docs.internal.api.rest.file.request.FileSaveRequest;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;
import rgo.cloud.docs.internal.api.storage.Language;

import java.io.IOException;

public class FileFacadeConverter {
    private FileFacadeConverter() {
    }

    public static DocumentLanguage convert(FileSaveRequest rq) throws IOException {
        return DocumentLanguage.builder()
                .document(Document.builder()
                        .fullName(rq.getFile().getFullFileName())
                        .name(rq.getFile().getFileName())
                        .extension(rq.getFile().getExtension())
                        .classification(Classification.builder()
                                .entityId(rq.getClassificationId())
                                .build())
                        .build())
                .language(Language.builder()
                        .entityId(rq.getLanguageId())
                        .build())
                .data(rq.getFile().getInputStream().readAllBytes())
                .build();
    }

    public static DocumentLanguage convert(FilePatchRequest rq) throws IOException {
        return DocumentLanguage.builder()
                .document(Document.builder()
                        .entityId(rq.getDocumentId())
                        .build())
                .language(Language.builder()
                        .entityId(rq.getLanguageId())
                        .build())
                .data(rq.getFile().getInputStream().readAllBytes())
                .build();
    }
}
