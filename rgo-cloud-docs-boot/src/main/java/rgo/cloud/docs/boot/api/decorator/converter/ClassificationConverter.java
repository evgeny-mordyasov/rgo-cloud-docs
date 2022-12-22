package rgo.cloud.docs.boot.api.decorator.converter;

import rgo.cloud.docs.internal.api.rest.classification.request.ClassificationSaveRequest;
import rgo.cloud.docs.internal.api.rest.classification.request.ClassificationUpdateRequest;
import rgo.cloud.docs.internal.api.storage.Classification;

public class ClassificationConverter {
    private ClassificationConverter() {
    }

    public static Classification convert(ClassificationSaveRequest rq) {
        return Classification.builder()
                .name(rq.getName())
                .build();
    }

    public static Classification convert(ClassificationUpdateRequest rq) {
        return Classification.builder()
                .entityId(rq.getEntityId())
                .name(rq.getName())
                .build();
    }
}
