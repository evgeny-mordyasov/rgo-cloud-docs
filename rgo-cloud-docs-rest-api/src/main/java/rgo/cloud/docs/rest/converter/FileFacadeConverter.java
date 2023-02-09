package rgo.cloud.docs.rest.converter;

import rgo.cloud.docs.rest.api.file.request.FilePatchRequest;
import rgo.cloud.docs.rest.api.file.request.FileSaveRequest;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.entity.Document;
import rgo.cloud.docs.db.api.entity.Translation;
import rgo.cloud.docs.db.api.entity.Language;

public final class FileFacadeConverter {
    private FileFacadeConverter() {
    }

    public static Translation convert(FileSaveRequest rq) {
        return Translation.builder()
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
                .data(rq.getFile().getData())
                .build();
    }

    public static Translation convert(FilePatchRequest rq) {
        return Translation.builder()
                .document(Document.builder()
                        .entityId(rq.getKey().getDocumentId())
                        .build())
                .language(Language.builder()
                        .entityId(rq.getKey().getLanguageId())
                        .build())
                .data(rq.getFile().getData())
                .build();
    }
}
