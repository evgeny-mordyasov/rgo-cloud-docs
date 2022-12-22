package rgo.cloud.docs.boot.api.decorator;

import rgo.cloud.common.api.rest.EmptySuccessfulResponse;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.docs.boot.service.LanguageService;
import rgo.cloud.docs.internal.api.rest.language.request.LanguageGetByIdRequest;
import rgo.cloud.docs.internal.api.rest.language.request.LanguageGetByNameRequest;
import rgo.cloud.docs.internal.api.rest.language.request.LanguageSaveRequest;
import rgo.cloud.docs.internal.api.rest.language.request.LanguageUpdateRequest;
import rgo.cloud.docs.internal.api.rest.language.response.LanguageGetEntityResponse;
import rgo.cloud.docs.internal.api.rest.language.response.LanguageGetListResponse;
import rgo.cloud.docs.internal.api.rest.language.response.LanguageModifyResponse;
import rgo.cloud.docs.internal.api.storage.Language;

import java.util.List;
import java.util.Optional;

import static rgo.cloud.docs.boot.api.decorator.converter.LanguageConverter.convert;

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
