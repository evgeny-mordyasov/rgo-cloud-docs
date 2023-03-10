package rgo.cloud.docs.boot.api.decorator;

import rgo.cloud.common.api.rest.EmptySuccessfulResponse;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.spring.annotation.Validate;
import rgo.cloud.docs.service.LanguageService;
import rgo.cloud.docs.rest.api.language.request.LanguageGetByIdRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageGetByNameRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageSaveRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageUpdateRequest;
import rgo.cloud.docs.rest.api.language.response.LanguageGetEntityResponse;
import rgo.cloud.docs.rest.api.language.response.LanguageGetListResponse;
import rgo.cloud.docs.rest.api.language.response.LanguageModifyResponse;
import rgo.cloud.docs.db.api.entity.Language;

import java.util.List;
import java.util.Optional;

import static rgo.cloud.docs.rest.converter.LanguageConverter.convert;

@Validate
public class LanguageServiceDecorator {
    private final LanguageService service;

    public LanguageServiceDecorator(LanguageService service) {
        this.service = service;
    }

    public LanguageGetListResponse findAll() {
        List<Language> list = service.findAll();
        return LanguageGetListResponse.success(list);
    }

    public Response findById(LanguageGetByIdRequest rq) {
        Optional<Language> opt = service.findById(rq.getEntityId());
        return resolve(opt);
    }

    public Response findByName(LanguageGetByNameRequest rq) {
        Optional<Language> opt = service.findByName(rq.getName());
        return resolve(opt);
    }

    public LanguageModifyResponse save(LanguageSaveRequest rq) {
        Language saved = service.save(convert(rq));
        return LanguageModifyResponse.success(saved);
    }

    public LanguageModifyResponse update(LanguageUpdateRequest rq) {
        Language updated = service.update(convert(rq));
        return LanguageModifyResponse.success(updated);
    }

    private static Response resolve(Optional<Language> found) {
        return found.isPresent()
                ? LanguageGetEntityResponse.success(found.get())
                : new EmptySuccessfulResponse();
    }
}
