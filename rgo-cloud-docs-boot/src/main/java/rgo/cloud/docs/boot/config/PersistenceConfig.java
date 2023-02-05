package rgo.cloud.docs.boot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import rgo.cloud.common.spring.storage.DbTxManager;
import rgo.cloud.docs.boot.config.properties.DbProperties;

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
}
