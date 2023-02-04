package rgo.cloud.docs.boot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.config.properties.DbProperties;
import rgo.cloud.docs.boot.storage.repository.ClassificationRepository;
import rgo.cloud.docs.boot.storage.repository.DocumentLanguageRepository;
import rgo.cloud.docs.boot.storage.repository.DocumentRepository;
import rgo.cloud.docs.boot.storage.repository.LanguageRepository;
import rgo.cloud.docs.boot.storage.repository.ReadingDocumentRepository;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfig {

    @Bean
    @Profile("test")
    public DataSource h2() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("docs-h2")
                .addScript("classpath:h2/init.sql")
                .build();
    }

    @Bean
    @Profile("!test")
    public DataSource pg(DbProperties dbProp) {
        HikariConfig hk = new HikariConfig();
        hk.setJdbcUrl(dbProp.getUrl());
        hk.setSchema(dbProp.getSchema());
        hk.setUsername(dbProp.getUsername());
        hk.setPassword(dbProp.getPassword());
        hk.setMaximumPoolSize(dbProp.getMaxPoolSize());

        return new HikariDataSource(hk);
    }

    @Bean
    public DbTxManager dbTxManager(DataSource ds) {
        return new DbTxManager(ds);
    }

    @Bean
    public LanguageRepository languageRepository(DbTxManager dbTxManager) {
        return new LanguageRepository(dbTxManager);
    }

    @Bean
    public ClassificationRepository classificationRepository(DbTxManager dbTxManager) {
        return new ClassificationRepository(dbTxManager);
    }

    @Bean
    public DocumentRepository documentRepository(DbTxManager dbTxManager) {
        return new DocumentRepository(dbTxManager);
    }

    @Bean
    public DocumentLanguageRepository documentLanguageRepository(DbTxManager dbTxManager) {
        return new DocumentLanguageRepository(dbTxManager);
    }

    @Bean
    public ReadingDocumentRepository readingDocumentRepository(DbTxManager dbTxManager) {
        return new ReadingDocumentRepository(dbTxManager);
    }
}
