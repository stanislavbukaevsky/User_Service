package com.github.stanislavbukaevsky.userservice.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки web интерфейса в Swagger
 */
@Configuration
@OpenAPIDefinition(info =
@Info(title = "Сервис для регистрации и авторизации", description = "Web интерфейс для регистрации и авторизации пользователей", contact =
@Contact(name = "Букаевский Станислав", email = "stanislavi18061989@gmail.com"))
)
@SecurityScheme(name = "JWT", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfiguration {
}
