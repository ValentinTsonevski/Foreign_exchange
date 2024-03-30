package com.example.foreign_exchange.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                description = "OpenApi documentation for foreign exchange api",
                title = "Open API specification Foreign Exchange",
                version = "1.0"),
        servers = {
                @Server(description = "Local ENV",url = "http://localhost:8080")
        }
)
public class OpenApiConfig {
}
