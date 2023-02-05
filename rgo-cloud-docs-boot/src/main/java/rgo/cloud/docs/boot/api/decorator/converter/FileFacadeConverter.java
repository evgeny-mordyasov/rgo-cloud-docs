package rgo.cloud.docs.boot.api.decorator.converter;

import org.springframework.web.multipart.MultipartFile;
import rgo.cloud.docs.boot.util.FileUtil;
import rgo.cloud.docs.internal.api.facade.MultipartFileDto;
import rgo.cloud.docs.internal.api.rest.file.request.FilePatchRequest;
import rgo.cloud.docs.internal.api.rest.file.request.FileSaveRequest;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.entity.Document;
import rgo.cloud.docs.db.api.entity.Translation;
import rgo.cloud.docs.db.api.entity.Language;

import java.io.IOException;

public final class FileFacadeConverter {
    private FileFacadeConverter() {
    }

    public static Translation convert(FileSaveRequest rq) throws IOException {
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
                .data(rq.getFile().getInputStream().readAllBytes())
                .build();
    }

    public static Translation convert(FilePatchRequest rq) throws IOException {
        return Translation.builder()
                .document(Document.builder()
                        .entityId(rq.getDocumentId())
                        .build())
                .language(Language.builder()
                        .entityId(rq.getLanguageId())
                        .build())
                .data(rq.getFile().getInputStream().readAllBytes())
                .build();
    }

    public static MultipartFileDto convert(MultipartFile file) throws IOException {
        return MultipartFileDto.builder()
                .withFullFileName(file.getOriginalFilename())
                .withFileName(FileUtil.getFileName(file.getOriginalFilename()))
                .withExtension(FileUtil.getFileExtension(file.getOriginalFilename()))
                .withInputStream(file.getInputStream())
                .withIsEmpty(file.isEmpty())
                .withSize(file.getSize())
                .build();
    }
}
