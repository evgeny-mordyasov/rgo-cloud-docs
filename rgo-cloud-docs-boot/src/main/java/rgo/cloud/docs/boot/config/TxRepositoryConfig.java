package rgo.cloud.docs.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.storage.repository.TxLanguageRepositoryDecorator;
import rgo.cloud.docs.boot.storage.repository.TxClassificationRepositoryDecorator;
import rgo.cloud.docs.boot.storage.repository.TxDocumentRepositoryDecorator;
import rgo.cloud.docs.boot.storage.repository.TxTranslationRepositoryDecorator;
import rgo.cloud.docs.boot.storage.repository.TxReadingDocumentRepositoryDecorator;
import rgo.cloud.docs.db.api.repository.LanguageRepository;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;
import rgo.cloud.docs.db.api.repository.DocumentRepository;
import rgo.cloud.docs.db.api.repository.TranslationRepository;
import rgo.cloud.docs.db.api.repository.ReadingDocumentRepository;

@Configuration
public class TxRepositoryConfig {

    @Bean
    public LanguageRepository languageRepository(LanguageRepository repo, DbTxManager tx) {
        return new TxLanguageRepositoryDecorator(repo, tx);
    }

    @Bean
    public ClassificationRepository classificationRepository(ClassificationRepository repo, DbTxManager tx) {
        return new TxClassificationRepositoryDecorator(repo, tx);
    }

    @Bean
    public DocumentRepository documentRepository(DocumentRepository repo, DbTxManager tx) {
        return new TxDocumentRepositoryDecorator(repo, tx);
    }

    @Bean
    public TranslationRepository translationRepository(TranslationRepository repo, DbTxManager tx) {
        return new TxTranslationRepositoryDecorator(repo, tx);
    }

    @Bean
    public ReadingDocumentRepository readingDocumentRepository(ReadingDocumentRepository repo, DbTxManager tx) {
        return new TxReadingDocumentRepositoryDecorator(repo, tx);
    }
}
