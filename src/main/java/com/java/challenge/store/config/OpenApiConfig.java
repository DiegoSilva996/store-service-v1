package com.java.challenge.store.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "store-service",
                description = "Store backend service for managing products and orders",
                termsOfService = "https://www.linkedin.com/in/diego-ernesto-silva-trujillo-70b892231/details/experience/",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Diego Silva",
                        url = "https://www.linkedin.com/in/diego-ernesto-silva-trujillo-70b892231",
                        email = "dsilva.tru96@gmail.com"
                ),
                license = @License(
                        name = "License by Diego Silva",
                        url = "https://www.linkedin.com/in/diego-ernesto-silva-trujillo-70b892231",
                        identifier = "Diego Silva"
                ),
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        description = "Develop",
                        url = "http://localhost:8080/"
                )
        }
)
public class OpenApiConfig {
}

