package com.aristurtle.question_service.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "Vacancies API",
        version = "1.0",
        description = "API для управления вакансиями"
    ),
    servers = [
        Server(url = "http://localhost:8585", description = "Local Server")
    ]
)
@Configuration
class OpenApiConfig