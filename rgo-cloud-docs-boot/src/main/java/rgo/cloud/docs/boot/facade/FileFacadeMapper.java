package rgo.cloud.docs.boot.facade;

import rgo.cloud.security.config.util.Endpoint;
import rgo.cloud.docs.internal.api.facade.FileDto;
import rgo.cloud.docs.internal.api.facade.ResourceDto;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;
import rgo.cloud.docs.internal.api.storage.Language;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class FileFacadeMapper {
    public static final String RESOURCE = "http://localhost:8080" +
            Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=%s&languageId=%s";

    private FileFacadeMapper() {
    }

    public static List<FileDto> convert(Set<Map.Entry<Long, List<DocumentLanguage>>> set) {
        return set.stream()
                .map(e -> convert(e.getValue().get(0).getDocument(), e.getValue()))
                .collect(Collectors.toList());
    }

    public static FileDto convert(Document document, List<DocumentLanguage> languages) {
        return FileDto.builder()
                .document(Document.builder()
                        .entityId(document.getEntityId())
                        .fullName(document.getFullName())
                        .name(document.getName())
                        .extension(document.getExtension())
                        .classification(Classification.builder()
                                .entityId(document.getClassification().getEntityId())
                                .name(document.getClassification().getName())
                                .build())
                        .build())
                .resources(languages.stream().map(dl ->
                        ResourceDto.builder()
                                .language(Language.builder()
                                        .entityId(dl.getLanguage().getEntityId())
                                        .name(dl.getLanguage().getName())
                                        .build())
                                .resource(resource(dl.getDocument().getEntityId(), dl.getLanguage().getEntityId()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private static String resource(Long documentId, Long languageId) {
        return String.format(RESOURCE, documentId, languageId);
    }
}
