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
import rgo.cloud.docs.service.*;
import rgo.cloud.docs.service.config.ServiceConfig;
import rgo.cloud.security.config.SecurityConfig;

@Configuration
@ConfigurationPropertiesScan
@Import(value = { SecurityConfig.class, AspectConfig.class, ServiceConfig.class})
public class ApplicationConfig {

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
