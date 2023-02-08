package rgo.cloud.docs.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import rgo.cloud.docs.db.api.repository.*;
import rgo.cloud.docs.db.config.PersistenceConfig;
import rgo.cloud.docs.service.*;

@Configuration
@Import(PersistenceConfig.class)
public class ServiceConfig {

    @Bean
    public LanguageService languageService(LanguageRepository languageRepository) {
        return new LanguageService(languageRepository);
    }

    @Bean
    public ClassificationService classificationService(ClassificationRepository classificationRepository) {
        return new ClassificationService(classificationRepository);
    }

    @Bean
    public DocumentService documentService(DocumentRepository documentRepository) {
        return new DocumentService(documentRepository);
    }

    @Bean
    public TranslationService translationService(TranslationRepository translationRepository) {
        return new TranslationService(translationRepository);
    }

    @Bean
    public ReadingDocumentService readingDocumentService(ReadingDocumentRepository readingDocumentRepository) {
        return new ReadingDocumentService(readingDocumentRepository);
    }
}
