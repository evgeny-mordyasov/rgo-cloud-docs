package rgo.cloud.docs.boot;

import org.apache.commons.lang3.RandomUtils;
import rgo.cloud.docs.db.api.entity.*;
import rgo.cloud.docs.rest.api.file.FileExtension;

import java.util.concurrent.ThreadLocalRandom;

import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;

public final class EntityGenerator {
    private EntityGenerator() {
    }

    public static Language createRandomLanguage() {
        return Language.builder()
                .name(randomString())
                .build();
    }

    public static Classification createRandomClassification() {
        return Classification.builder()
                .name(randomString())
                .build();
    }

    public static Document createRandomDocument(Classification classification) {
        return Document.builder()
                .fullName(randomString())
                .name(randomString())
                .extension(generateExtension())
                .classification(classification)
                .build();
    }

    public static String generateExtension() {
        return FileExtension.values()[ThreadLocalRandom.current().nextInt(FileExtension.values().length)]
                .name();
    }

    public static Translation createRandomTranslation(Document document, Language language) {
        return Translation.builder()
                .document(document)
                .language(language)
                .data(RandomUtils.nextBytes(256))
                .build();
    }

    public static ReadingDocument createRandomReadingDocument(Long documentId, Long languageId) {
        return ReadingDocument.builder()
                .documentId(documentId)
                .languageId(languageId)
                .build();
    }
}
