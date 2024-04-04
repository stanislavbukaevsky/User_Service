package com.github.stanislavbukaevsky.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс-DTO для ответа с информацией об аутентифицированном пользователе
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "Объект аутентификации для ответа пользователю")
public class AuthenticationResponseDto {
    @Schema(description = "Уникальный идентификатор пользователя")
    private Long id;
    @Schema(description = "Имя пользователя")
    private String firstName;
    @Schema(description = "Фамилия пользователя")
    private String lastName;
    @Schema(description = "Электронная почта пользователя при регистрации, аутентификации и авторизации")
    private String email;
    @Schema(description = "Пароль пользователя")
    private String password;
    @Schema(description = "Роль пользователя")
    private String role;
    @Schema(description = "Access токен пользователя")
    private String accessToken;
    @Schema(description = "Refresh токен пользователя")
    private String refreshToken;
    @Schema(description = "Время истечения срока действия access токена")
    private LocalDateTime expiresAtAccess;
    @Schema(description = "Время истечения срока действия refresh токена")
    private LocalDateTime expiresAtRefresh;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationResponseDto that = (AuthenticationResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(role, that.role) && Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken) && Objects.equals(expiresAtAccess, that.expiresAtAccess) && Objects.equals(expiresAtRefresh, that.expiresAtRefresh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, role, accessToken, refreshToken, expiresAtAccess, expiresAtRefresh);
    }
}
