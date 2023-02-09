package rgo.cloud.docs.boot.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.docs.boot.api.decorator.FileFacadeDecorator;
import rgo.cloud.docs.boot.facade.FileResource;
import rgo.cloud.docs.db.api.entity.TranslationKey;
import rgo.cloud.docs.model.facade.MultipartFileDto;
import rgo.cloud.docs.rest.api.file.request.FileGetByDocumentIdRequest;
import rgo.cloud.docs.rest.api.file.request.FileGetByClassificationIdRequest;
import rgo.cloud.docs.rest.api.file.request.FileGetFreeLanguagesByDocumentIdRequest;
import rgo.cloud.docs.rest.api.file.request.FileGetResourceRequest;
import rgo.cloud.docs.rest.api.file.request.FileSaveRequest;
import rgo.cloud.docs.rest.api.file.request.FilePatchRequest;
import rgo.cloud.docs.rest.api.file.request.FileDeleteByKeyRequest;
import rgo.cloud.docs.rest.api.file.request.FileDeleteByDocumentIdRequest;
import rgo.cloud.security.config.util.Endpoint;

import java.io.IOException;
import java.util.Optional;

import static rgo.cloud.common.api.rest.BaseErrorResponse.handleException;
import static rgo.cloud.common.api.util.RequestUtil.JSON;
import static rgo.cloud.common.api.util.RequestUtil.execute;
import static rgo.cloud.docs.boot.api.decorator.converter.FileFacadeConverter.convert;

@RestController
@RequestMapping(Endpoint.File.BASE_URL)
public class FileRestController {
    private final FileFacadeDecorator service;

    public FileRestController(FileFacadeDecorator service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    public Response findAll() {
        return execute(service::findAll);
    }

    @GetMapping(value = Endpoint.File.DOCUMENT_ID_VARIABLE, produces = JSON)
    public Response findByDocumentId(@PathVariable Long documentId) {
        return execute(() -> service.findByDocumentId(new FileGetByDocumentIdRequest(documentId)));
    }

    @GetMapping(params = "classificationId", produces = JSON)
    public Response findByClassificationId(@RequestParam("classificationId") Long classificationId) {
        return execute(() -> service.findByClassificationId(new FileGetByClassificationIdRequest(classificationId)));
    }

    @GetMapping(value = Endpoint.File.FREE_LANGUAGES, produces = JSON)
    public Response getFreeLanguages(@PathVariable Long documentId) {
        return execute(() -> service.getFreeLanguages(new FileGetFreeLanguagesByDocumentIdRequest(documentId)));
    }

    @GetMapping(value = Endpoint.File.RESOURCE)
    public ResponseEntity<?> findResource(@RequestParam(name = "documentId") Long documentId,
                                          @RequestParam(name = "languageId") Long languageId) {
        try {
            TranslationKey key = TranslationKey.builder()
                    .documentId(documentId)
                    .languageId(languageId)
                    .build();
            FileResource resource = service.load(new FileGetResourceRequest(key));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFullFileName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new ByteArrayResource(resource.getData()));
        } catch (Exception e) {
            return ResponseEntity.of(Optional.of(handleException(e)));
        }
    }

    @PostMapping(produces = JSON)
    public Response save(@RequestParam("file") MultipartFile file,
                         @RequestParam("languageId") Long languageId,
                         @RequestParam("classificationId") Long classificationId) {
        return execute(() -> {
            FileSaveRequest rq = FileSaveRequest.builder()
                    .file(readFile(file))
                    .languageId(languageId)
                    .classificationId(classificationId)
                    .build();

            return service.save(rq);
        });
    }

    @PatchMapping(produces = JSON)
    public Response patch(@RequestParam("file") MultipartFile file,
                          @RequestParam("documentId") Long documentId,
                          @RequestParam("languageId") Long languageId) {
        return execute(() -> {
            FilePatchRequest rq = FilePatchRequest.builder()
                    .file(readFile(file))
                    .key(TranslationKey.builder()
                            .documentId(documentId)
                            .languageId(languageId)
                            .build())
                    .build();

            return service.patch(rq);
        });
    }

    @DeleteMapping(produces = JSON)
    public Response deleteByKey(@RequestParam(name = "documentId") Long documentId,
                                @RequestParam(name = "languageId") Long languageId) {
        TranslationKey key = TranslationKey.builder()
                .documentId(documentId)
                .languageId(languageId)
                .build();

        return execute(() -> service.deleteByKey(new FileDeleteByKeyRequest(key)));
    }

    @DeleteMapping(value = Endpoint.ENTITY_ID_VARIABLE, produces = JSON)
    public Response deleteByDocumentId(@PathVariable Long entityId) {
        return execute(() -> service.deleteByDocumentId(new FileDeleteByDocumentIdRequest(entityId)));
    }

    private MultipartFileDto readFile(MultipartFile file) {
        try {
            return convert(file);
        } catch (IOException e) {
            throw new UnpredictableException("Failed to read file.");
        }
    }
}
