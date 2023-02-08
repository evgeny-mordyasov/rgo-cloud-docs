package rgo.cloud.docs.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import rgo.cloud.common.api.properties.DbProperties;
import rgo.cloud.common.spring.storage.DbTxManager;

import javax.sql.DataSource;

@Configuration
@Import({ NativeRepositoryConfig.class, TxRepositoryConfig.class })
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
        hk.setJdbcUrl(dbProp.url());
        hk.setSchema(dbProp.schema());
        hk.setUsername(dbProp.username());
        hk.setPassword(dbProp.password());
        hk.setMaximumPoolSize(dbProp.maxPoolSize());

        return new HikariDataSource(hk);
    }

    @Bean
    public DbTxManager dbTxManager(DataSource ds) {
        return new DbTxManager(ds);
    }
}
