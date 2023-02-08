package rgo.cloud.docs.db.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import rgo.cloud.common.api.properties.DbProperties;

@ConstructorBinding
@ConfigurationProperties(prefix = "module-properties.persistence")
public class PostgresProperties implements DbProperties {
    private final String url;
    private final String schema;
    private final String username;
    private final String password;
    private final int maxPoolSize;

    public PostgresProperties(String url, String schema, String username, String password, int maxPoolSize) {
        this.url = url;
        this.schema = schema;
        this.username = username;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String schema() {
        return schema;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public int maxPoolSize() {
        return maxPoolSize;
    }
}
