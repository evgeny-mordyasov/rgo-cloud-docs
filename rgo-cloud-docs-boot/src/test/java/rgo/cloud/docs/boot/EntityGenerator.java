package rgo.cloud.docs.boot;

import org.apache.commons.lang3.RandomUtils;
import rgo.cloud.docs.internal.api.constant.FileExtension;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;
import rgo.cloud.docs.internal.api.storage.Language;

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

    public static DocumentLanguage createRandomDocumentLanguage(Document document, Language language) {
        return DocumentLanguage.builder()
                .document(document)
                .language(language)
                .data(RandomUtils.nextBytes(256))
                .build();
    }
}
