package rgo.cloud.docs.boot.api.decorator;

import rgo.cloud.common.api.rest.EmptySuccessfulResponse;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.spring.annotation.Transactional;
import rgo.cloud.common.spring.annotation.Validate;
import rgo.cloud.docs.facade.FileFacade;
import rgo.cloud.docs.facade.api.FileResource;
import rgo.cloud.docs.facade.api.FileDto;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.docs.rest.api.file.request.*;
import rgo.cloud.docs.rest.api.file.response.*;

import java.util.List;
import java.util.Optional;

import static rgo.cloud.docs.rest.converter.FileFacadeConverter.convert;

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

    public FileGetListResponse findByFullName(FileGetByFullNameRequest rq) {
        List<FileDto> list = facade.findByFullName(rq.getName());
        return FileGetListResponse.success(list);
    }

    public Response findByDocumentId(FileGetByDocumentIdRequest rq) {
        Optional<FileDto> opt = facade.findByDocumentId(rq.getDocumentId());

        return opt.isPresent()
                ? FileGetEntityResponse.success(opt.get())
                : new EmptySuccessfulResponse();
    }

    public Response getFreeLanguages(FileGetFreeLanguagesByDocumentIdRequest rq) {
        List<Language> languages = facade.getFreeLanguages(rq.getDocumentId());
        return FileGetFreeLanguagesByDocumentIdResponse.success(languages);
    }

    public FileResource load(FileGetResourceRequest rq) {
        return facade.load(rq.getKey());
    }

    @Transactional
    public Response save(FileSaveRequest rq) {
        FileDto saved = facade.save(convert(rq));
        return FileModifyResponse.success(saved);
    }

    @Transactional
    public Response patch(FilePatchRequest rq) {
        FileDto updated = facade.patch(convert(rq));
        return FileModifyResponse.success(updated);
    }

    public FileModifyResponse patchFileName(FilePatchNameRequest rq) {
        FileDto updated = facade.patchFileName(convert(rq));
        return FileModifyResponse.success(updated);
    }

    public FileDeleteResponse deleteByDocumentId(FileDeleteByDocumentIdRequest rq) {
        facade.deleteByDocumentId(rq.getDocumentId());
        return FileDeleteResponse.success();
    }

    @Transactional
    public Response deleteByKey(FileDeleteByKeyRequest rq) {
        facade.deleteByKey(rq.getKey());
        return FileDeleteResponse.success();
    }
}
