package rgo.cloud.docs.boot.api;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.docs.boot.api.decorator.FileFacadeDecorator;
import rgo.cloud.docs.facade.api.FileResource;
import rgo.cloud.docs.boot.utils.FileUtils;
import rgo.cloud.docs.db.api.entity.TranslationKey;
import rgo.cloud.docs.facade.api.MultipartFileDto;
import rgo.cloud.docs.rest.api.file.request.*;
import rgo.cloud.security.config.util.Endpoint;

import java.io.IOException;

import static rgo.cloud.common.spring.util.RequestUtil.*;

@Hidden
@RestController
@RequestMapping(Endpoint.File.BASE_URL)
public class FileRestController {
    private final FileFacadeDecorator service;

    public FileRestController(FileFacadeDecorator service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<Response> findAll() {
        return execute(service::findAll);
    }

    @GetMapping(value = Endpoint.File.DOCUMENT_ID_VARIABLE, produces = JSON)
    public ResponseEntity<Response> findByDocumentId(@PathVariable Long documentId) {
        return execute(() -> service.findByDocumentId(new FileGetByDocumentIdRequest(documentId)));
    }

    @GetMapping(params = "classificationId", produces = JSON)
    public ResponseEntity<Response> findByClassificationId(@RequestParam("classificationId") Long classificationId) {
        return execute(() -> service.findByClassificationId(new FileGetByClassificationIdRequest(classificationId)));
    }

    @GetMapping(value = Endpoint.File.FREE_LANGUAGES, produces = JSON)
    public ResponseEntity<Response> getFreeLanguages(@PathVariable Long documentId) {
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
            return errorResponse(e);
        }
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<Response> save(@RequestParam("file") MultipartFile file,
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
    public ResponseEntity<Response> patch(@RequestParam("file") MultipartFile file,
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

    @PatchMapping(value = Endpoint.File.UPDATE_NAME, produces = JSON)
    public ResponseEntity<Response> patchFileName(@RequestParam("documentId") Long documentId,
                                                  @RequestParam("fileName") String fileName) {
        FilePatchNameRequest rq = FilePatchNameRequest.builder()
                .documentId(documentId)
                .fileName(fileName)
                .build();

        return execute(() -> service.patchFileName(rq));
    }

    @DeleteMapping(produces = JSON)
    public ResponseEntity<Response> deleteByKey(@RequestParam(name = "documentId") Long documentId,
                                @RequestParam(name = "languageId") Long languageId) {
        TranslationKey key = TranslationKey.builder()
                .documentId(documentId)
                .languageId(languageId)
                .build();

        return execute(() -> service.deleteByKey(new FileDeleteByKeyRequest(key)));
    }

    @DeleteMapping(value = Endpoint.ENTITY_ID_VARIABLE, produces = JSON)
    public ResponseEntity<Response> deleteByDocumentId(@PathVariable Long entityId) {
        return execute(() -> service.deleteByDocumentId(new FileDeleteByDocumentIdRequest(entityId)));
    }

    private MultipartFileDto readFile(MultipartFile file) {
        try {
            return convert(file);
        } catch (IOException e) {
            throw new UnpredictableException("Failed to read file.");
        }
    }

    private static MultipartFileDto convert(MultipartFile file) throws IOException {
        return MultipartFileDto.builder()
                .fullFileName(file.getOriginalFilename())
                .fileName(FileUtils.getFileName(file.getOriginalFilename()))
                .extension(FileUtils.getFileExtension(file.getOriginalFilename()))
                .data(file.getInputStream().readAllBytes())
                .isEmpty(file.isEmpty())
                .size(file.getSize())
                .build();
    }
}
