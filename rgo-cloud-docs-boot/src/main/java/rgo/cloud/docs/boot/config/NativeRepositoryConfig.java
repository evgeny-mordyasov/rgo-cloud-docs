package rgo.cloud.docs.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.repository.natural.PostgresLanguageRepository;
import rgo.cloud.docs.boot.storage.repository.natural.PostgresClassificationRepository;
import rgo.cloud.docs.boot.storage.repository.natural.PostgresDocumentRepository;
import rgo.cloud.docs.boot.storage.repository.natural.PostgresTranslationRepository;
import rgo.cloud.docs.boot.storage.repository.natural.PostgresReadingDocumentRepository;
import rgo.cloud.docs.db.api.repository.LanguageRepository;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;
import rgo.cloud.docs.db.api.repository.DocumentRepository;
import rgo.cloud.docs.db.api.repository.TranslationRepository;
import rgo.cloud.docs.db.api.repository.ReadingDocumentRepository;

@Configuration
public class NativeRepositoryConfig {

    @Bean
    public LanguageRepository nativeLanguageRepository(DbTxManager tx) {
        return new PostgresLanguageRepository(tx);
    }

    @Bean
    public ClassificationRepository nativeClassificationRepository(DbTxManager tx) {
        return new PostgresClassificationRepository(tx);
    }

    @Bean
    public DocumentRepository nativeDocumentRepository(DbTxManager tx) {
        return new PostgresDocumentRepository(tx);
    }

    @Bean
    public TranslationRepository nativeTranslationRepository(DbTxManager tx) {
        return new PostgresTranslationRepository(tx);
    }

    @Bean
    public ReadingDocumentRepository nativeReadingDocumentRepository(DbTxManager tx) {
        return new PostgresReadingDocumentRepository(tx);
    }
}
