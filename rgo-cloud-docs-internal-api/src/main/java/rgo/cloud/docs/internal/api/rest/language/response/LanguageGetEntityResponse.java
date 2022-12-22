package rgo.cloud.docs.internal.api.rest.language.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.common.api.rest.Status;
import rgo.cloud.docs.internal.api.storage.Language;

@Builder
@Getter
@ToString
public class LanguageGetEntityResponse implements Response {
    private final Status status;
    private final Language object;

    public static LanguageGetEntityResponse success(Language language) {
        return LanguageGetEntityResponse.builder()
                .status(Status.success())
                .object(language)
                .build();
    }
}
