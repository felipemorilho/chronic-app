package com.chronicpain.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "ChronicPain API",
        version = "v1",
        description = "API para registro de dor, relatórios e usuários.",
        contact = Contact(
            name = "Felipe M. de Castro",
            email = "felipemdecastro@gmail.com"
        )
    )
)
class OpenApiConfig