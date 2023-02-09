package rgo.cloud.docs.rest.converter;

import rgo.cloud.docs.rest.api.language.request.LanguageSaveRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageUpdateRequest;
import rgo.cloud.docs.db.api.entity.Language;

public final class LanguageConverter {
    private LanguageConverter() {
    }

    public static Language convert(LanguageSaveRequest rq) {
        return Language.builder()
                .name(rq.getName())
                .build();
    }

    public static Language convert(LanguageUpdateRequest rq) {
        return Language.builder()
                .entityId(rq.getEntityId())
                .name(rq.getName())
                .build();
    }
}
