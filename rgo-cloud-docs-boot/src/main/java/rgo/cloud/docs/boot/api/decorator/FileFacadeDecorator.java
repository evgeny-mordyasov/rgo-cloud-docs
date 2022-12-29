package rgo.cloud.docs.boot.api.decorator;

import rgo.cloud.common.api.rest.EmptySuccessfulResponse;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.spring.aspect.Validate;
import rgo.cloud.docs.boot.facade.FileFacade;
import rgo.cloud.docs.boot.facade.FileResource;
import rgo.cloud.docs.internal.api.facade.FileDto;
import rgo.cloud.docs.internal.api.rest.file.request.*;
import rgo.cloud.docs.internal.api.rest.file.response.FileDeleteResponse;
import rgo.cloud.docs.internal.api.rest.file.response.FileGetEntityResponse;
import rgo.cloud.docs.internal.api.rest.file.response.FileGetListResponse;
import rgo.cloud.docs.internal.api.rest.file.response.FileModifyResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static rgo.cloud.docs.boot.api.decorator.converter.FileFacadeConverter.convert;

@Validate
public class FileFacadeDecorator {
    private final FileFacade facade;

    public FileFacadeDecorator(FileFacade facade) {
        this.facade = facade;
    }

    public FileGetListResponse findAll() {
        List<FileDto> list = facade.findAll();
        return FileGetListResponse.success(list);
    }

    public FileGetListResponse findByClassificationId(FileGetByClassificationIdRequest rq) {
        List<FileDto> list = facade.findByClassificationId(rq.getClassificationId());
        return FileGetListResponse.success(list);
    }

    public Response findByDocumentId(FileGetByDocumentIdRequest rq) {
        Optional<FileDto> opt = facade.findByDocumentId(rq.getDocumentId());

        return opt.isPresent()
                ? FileGetEntityResponse.success(opt.get())
                : new EmptySuccessfulResponse();
    }

    public FileResource load(FileGetResourceRequest rq) {
        return facade.load(rq.getDocumentId(), rq.getLanguageId());
    }

    public FileModifyResponse save(FileSaveRequest rq) throws IOException {
        FileDto saved = facade.save(convert(rq));
        return FileModifyResponse.success(saved);
    }

    public FileModifyResponse patch(FilePatchRequest rq) throws IOException {
        FileDto updated = facade.patch(convert(rq));
        return FileModifyResponse.success(updated);
    }

    public FileDeleteResponse deleteByDocumentId(FileDeleteByDocumentIdRequest rq) {
        facade.deleteByDocumentId(rq.getDocumentId());
        return FileDeleteResponse.success();
    }

    public FileDeleteResponse deleteByDocumentIdAndLanguageId(FileDeleteByDocumentIdAndLanguageIdRequest rq) {
        facade.deleteByDocumentIdAndLanguageId(rq.getDocumentId(), rq.getLanguageId());
        return FileDeleteResponse.success();
    }
}
