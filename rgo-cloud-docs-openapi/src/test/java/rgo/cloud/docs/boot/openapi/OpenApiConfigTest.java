package rgo.cloud.docs.boot.openapi;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpenApiConfigTest {

    @Test
    public void fileExists() {
        URL url = Thread.currentThread().getContextClassLoader().getResource(OpenApiConfig.PATH);
        assertNotNull(url);
    }
}
