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
import rgo.cloud.docs.boot.storage.repository.ClassificationRepository;
import rgo.cloud.docs.boot.storage.repository.DocumentLanguageRepository;
import rgo.cloud.docs.boot.storage.repository.DocumentRepository;
import rgo.cloud.docs.boot.storage.repository.LanguageRepository;
import rgo.cloud.security.config.SecurityConfig;

@Configuration
@ConfigurationPropertiesScan
@Import(value = { SecurityConfig.class, AspectConfig.class })
public class ApplicationConfig {

    @Bean
    public LanguageService languageService(LanguageRepository repository) {
        return new LanguageService(repository);
    }

    @Bean
    public ClassificationService classificationService(ClassificationRepository repository) {
        return new ClassificationService(repository);
    }

    @Bean
    public DocumentService documentService(DocumentRepository repository) {
        return new DocumentService(repository);
    }

    @Bean
    public DocumentLanguageService documentLanguageService(DocumentLanguageRepository repository) {
        return new DocumentLanguageService(repository);
    }

    @Bean
    public FileFacade fileFacade(
            DocumentService documentService,
            DocumentLanguageService dlService,
            LanguageService languageService
    ) {
        return new FileFacade(documentService, dlService, languageService);
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
