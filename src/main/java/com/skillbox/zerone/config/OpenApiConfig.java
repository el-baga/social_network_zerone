package com.skillbox.zerone.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Zerone API",
                version = "1.0",
                description = "API for social network",
                contact = @Contact(url = "http://217.107.219.242:8080/", name = "Java Pro 45 group", email = "hello@skillbox.ru")
        ),
        servers = {
                @Server(
                        description = "localhost",
                        url = "http://localhost:8086/"
                ),
                @Server(
                        description = "server",
                        url = "http://217.107.219.242:8086/"
                )
        }
)
@SecurityScheme(
        name = "JWT Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
