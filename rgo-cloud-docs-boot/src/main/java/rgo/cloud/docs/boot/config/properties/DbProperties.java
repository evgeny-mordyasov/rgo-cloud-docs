package rgo.cloud.docs.boot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "module-properties.persistence")
public class DbProperties {
    private final String url;
    private final String scheme;
    private final String username;
    private final String password;
    private final int maxPoolSize;

    public DbProperties(String url, String scheme, String username, String password, int maxPoolSize) {
        this.url = url;
        this.scheme = scheme;
        this.username = username;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
    }

    public String getUrl() {
        return url;
    }

    public String getScheme() {
        return scheme;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }
}
