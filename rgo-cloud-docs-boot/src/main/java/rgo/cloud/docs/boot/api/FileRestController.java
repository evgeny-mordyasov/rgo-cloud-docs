package rgo.cloud.docs.boot.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.security.config.util.Endpoint;
import rgo.cloud.docs.boot.api.decorator.FileFacadeDecorator;
import rgo.cloud.docs.boot.facade.FileResource;
import rgo.cloud.docs.boot.util.FileUtil;
import rgo.cloud.docs.internal.api.facade.MultipartFileDto;
import rgo.cloud.docs.internal.api.rest.file.request.*;

import java.io.IOException;
import java.util.Optional;

import static rgo.cloud.common.api.rest.BaseErrorResponse.handleException;
import static rgo.cloud.common.api.util.RequestUtil.JSON;
import static rgo.cloud.common.api.util.RequestUtil.execute;

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

    @GetMapping(value = Endpoint.File.RESOURCE, produces = JSON)
    public ResponseEntity<?> findResource(@RequestParam(name = "documentId") Long documentId,
                                          @RequestParam(name = "languageId") Long languageId) {
        try {
            FileResource resource = service.load(new FileGetResourceRequest(documentId, languageId));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFullFileName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource.getData());
        } catch (Exception e) {
            return ResponseEntity.of(Optional.of(handleException(e)));
        }
    }

    @PostMapping(produces = JSON)
    public Response save(@RequestParam("file") MultipartFile file,
                         @RequestParam("languageId") Long languageId,
                         @RequestParam("classificationId") Long classificationId) {
        return execute(() -> {
            try {
                FileSaveRequest rq = FileSaveRequest.builder()
                        .file(convert(file))
                        .languageId(languageId)
                        .classificationId(classificationId)
                        .build();

                return service.save(rq);
            } catch (Exception e) {
                return handleException(e);
            }
        });
    }

    @PatchMapping(produces = JSON)
    public Response patch(@RequestParam("file") MultipartFile file,
                          @RequestParam("documentId") Long documentId,
                          @RequestParam("languageId") Long languageId) {
        return execute(() -> {
            try {
                FilePatchRequest rq = FilePatchRequest.builder()
                        .file(convert(file))
                        .documentId(documentId)
                        .languageId(languageId)
                        .build();

                return service.patch(rq);
            } catch (Exception e) {
                return handleException(e);
            }
        });
    }

    @DeleteMapping(produces = JSON)
    public Response deleteByDocumentIdAndLanguageId(@RequestParam(name = "documentId") Long documentId,
                                                    @RequestParam(name = "languageId") Long languageId) {
        return execute(() -> service.deleteByDocumentIdAndLanguageId(
                new FileDeleteByDocumentIdAndLanguageIdRequest(documentId, languageId)));
    }

    @DeleteMapping(value = Endpoint.ENTITY_ID_VARIABLE, produces = JSON)
    public Response deleteByDocumentId(@PathVariable Long entityId) {
        return execute(() -> service.deleteByDocumentId(new FileDeleteByDocumentIdRequest(entityId)));
    }

    private MultipartFileDto convert(MultipartFile file) throws IOException {
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
