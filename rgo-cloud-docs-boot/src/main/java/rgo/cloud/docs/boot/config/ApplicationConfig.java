package rgo.cloud.docs.boot.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import rgo.cloud.common.spring.config.AspectConfig;
import rgo.cloud.docs.boot.api.decorator.ClassificationServiceDecorator;
import rgo.cloud.docs.boot.api.decorator.FileFacadeDecorator;
import rgo.cloud.docs.boot.api.decorator.LanguageServiceDecorator;
import rgo.cloud.docs.boot.facade.FileFacade;
import rgo.cloud.docs.boot.service.*;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;
import rgo.cloud.docs.db.api.repository.TranslationRepository;
import rgo.cloud.docs.db.api.repository.DocumentRepository;
import rgo.cloud.docs.db.api.repository.LanguageRepository;
import rgo.cloud.docs.db.api.repository.ReadingDocumentRepository;
import rgo.cloud.security.config.SecurityConfig;

@Configuration
@ConfigurationPropertiesScan
@Import(value = { SecurityConfig.class, AspectConfig.class })
public class ApplicationConfig {

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

    @Bean
    public FileFacade fileFacade(
            DocumentService documentService,
            TranslationService translationService,
            LanguageService languageService,
            ReadingDocumentService readingDocumentService
    ) {
        return new FileFacade(documentService, translationService, languageService, readingDocumentService);
    }

    @Bean
    public LanguageServiceDecorator languageDecorator(LanguageService service) {
        return new LanguageServiceDecorator(service);
    }

    @Bean
    public ClassificationServiceDecorator classificationDecorator(ClassificationService service) {
        return new ClassificationServiceDecorator(service);
    }

    @Bean
    public FileFacadeDecorator fileFacadeDecorator(FileFacade facade) {
        return new FileFacadeDecorator(facade);
    }
}
