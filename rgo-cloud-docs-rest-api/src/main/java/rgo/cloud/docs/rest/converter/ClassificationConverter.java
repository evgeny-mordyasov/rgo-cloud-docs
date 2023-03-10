package rgo.cloud.docs.rest.converter;

import rgo.cloud.docs.rest.api.classification.request.ClassificationSaveRequest;
import rgo.cloud.docs.rest.api.classification.request.ClassificationUpdateRequest;
import rgo.cloud.docs.db.api.entity.Classification;

public final class ClassificationConverter {
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
