package rgo.cloud.docs.boot.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    static final String PATH = "openapi.yaml";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPIV3Parser()
                .read(PATH);
    }
}
