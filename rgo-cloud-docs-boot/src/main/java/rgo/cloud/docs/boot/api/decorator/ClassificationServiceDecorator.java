package rgo.cloud.docs.boot.api.decorator;

import rgo.cloud.common.api.rest.EmptySuccessfulResponse;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.spring.annotation.Validate;
import rgo.cloud.docs.boot.service.ClassificationService;
import rgo.cloud.docs.rest.api.classification.request.*;
import rgo.cloud.docs.rest.api.classification.response.ClassificationDeleteResponse;
import rgo.cloud.docs.rest.api.classification.response.ClassificationGetEntityResponse;
import rgo.cloud.docs.rest.api.classification.response.ClassificationGetListResponse;
import rgo.cloud.docs.rest.api.classification.response.ClassificationModifyResponse;
import rgo.cloud.docs.db.api.entity.Classification;

import java.util.List;
import java.util.Optional;

import static rgo.cloud.docs.boot.api.decorator.converter.ClassificationConverter.convert;

@Validate
public class ClassificationServiceDecorator {
    private final ClassificationService service;

    public ClassificationServiceDecorator(ClassificationService service) {
        this.service = service;
    }

    public ClassificationGetListResponse findAll() {
        List<Classification> list = service.findAll();
        return ClassificationGetListResponse.success(list);
    }

    public Response findById(ClassificationGetByIdRequest rq) {
        Optional<Classification> opt = service.findById(rq.getEntityId());
        return resolve(opt);
    }

    public Response findByName(ClassificationGetByNameRequest rq) {
        Optional<Classification> opt = service.findByName(rq.getName());
        return resolve(opt);
    }

    public ClassificationModifyResponse save(ClassificationSaveRequest rq) {
        Classification saved = service.save(convert(rq));
        return ClassificationModifyResponse.success(saved);
    }

    public ClassificationModifyResponse update(ClassificationUpdateRequest rq) {
        Classification updated = service.update(convert(rq));
        return ClassificationModifyResponse.success(updated);
    }

    public ClassificationDeleteResponse deleteById(ClassificationDeleteByIdRequest rq) {
        service.deleteById(rq.getEntityId());
        return ClassificationDeleteResponse.success();
    }

    private static Response resolve(Optional<Classification> found) {
        return found.isPresent()
                ? ClassificationGetEntityResponse.success(found.get())
                : new EmptySuccessfulResponse();
    }
}
