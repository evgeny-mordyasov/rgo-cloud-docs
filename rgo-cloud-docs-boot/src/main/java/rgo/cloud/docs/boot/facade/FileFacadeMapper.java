package rgo.cloud.docs.boot.facade;

import rgo.cloud.security.config.util.Endpoint;
import rgo.cloud.docs.rest.api.facade.FileDto;
import rgo.cloud.docs.rest.api.facade.ResourceDto;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.entity.Document;
import rgo.cloud.docs.db.api.entity.Translation;
import rgo.cloud.docs.db.api.entity.Language;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class FileFacadeMapper {
    public static final String RESOURCE = "http://localhost:8091" +
            Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=%s&languageId=%s";

    private FileFacadeMapper() {
    }

    public static List<FileDto> convert(Set<Map.Entry<Long, List<Translation>>> set) {
        return set.stream()
                .map(e -> convert(e.getValue().get(0).getDocument(), e.getValue()))
                .collect(Collectors.toList());
    }

    public static FileDto convert(Document document, List<Translation> translations) {
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
                .resources(translations.stream().map(tr ->
                        ResourceDto.builder()
                                .language(Language.builder()
                                        .entityId(tr.getLanguage().getEntityId())
                                        .name(tr.getLanguage().getName())
                                        .build())
                                .resource(resource(tr.getDocument().getEntityId(), tr.getLanguage().getEntityId()))
                                .downloads(tr.getDownloads())
                                .build())
                        .collect(Collectors.toList()))
                .downloads(translations.stream()
                        .map(Translation::getDownloads)
                        .reduce(0L, Long::sum))
                .build();
    }

    private static String resource(Long documentId, Long languageId) {
        return String.format(RESOURCE, documentId, languageId);
    }
}
