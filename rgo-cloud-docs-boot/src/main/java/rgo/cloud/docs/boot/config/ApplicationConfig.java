package rgo.cloud.docs.boot.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rgo.cloud.docs.boot.service.ClassificationService;
import rgo.cloud.docs.boot.service.DocumentLanguageService;
import rgo.cloud.docs.boot.service.DocumentService;
import rgo.cloud.docs.boot.service.LanguageService;
import rgo.cloud.docs.boot.storage.repository.ClassificationRepository;
import rgo.cloud.docs.boot.storage.repository.DocumentLanguageRepository;
import rgo.cloud.docs.boot.storage.repository.DocumentRepository;
import rgo.cloud.docs.boot.storage.repository.LanguageRepository;

@Configuration
@ConfigurationPropertiesScan
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
}
