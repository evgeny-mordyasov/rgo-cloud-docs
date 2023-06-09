package rgo.cloud.docs.db.utils;

import org.apache.commons.lang3.RandomUtils;
import rgo.cloud.docs.db.api.entity.*;

import java.util.concurrent.ThreadLocalRandom;

import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;

public final class EntityGenerator {
    private static final String[] EXTENSIONS = {"DOC", "DOCX", "RTF", "TXT", "PDF"};

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
        String name = randomString();
        String extension = generateExtension();
        String fullName = name + "." + extension;

        return Document.builder()
                .fullName(fullName)
                .name(name)
                .extension(extension)
                .classification(classification)
                .build();
    }

    public static Document createRandomDocument(Classification classification, String extension) {
        String name = randomString();
        String fullName = name + "." + extension;

        return Document.builder()
                .fullName(fullName)
                .name(name)
                .extension(extension)
                .classification(classification)
                .build();
    }

    public static String generateExtension() {
        return EXTENSIONS[ThreadLocalRandom.current().nextInt(EXTENSIONS.length)];
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
                .key(TranslationKey.builder()
                        .documentId(documentId)
                        .languageId(languageId)
                        .build())
                .build();
    }
}
