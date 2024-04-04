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
 * Класс-DTO для получения информации о пользователе и получении access-токена
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "Объект для получения нового access-токена")
public class AccessTokenResponseDto {
    @Schema(description = "Электронная почта пользователя при регистрации, аутентификации и авторизации")
    private String email;
    @Schema(description = "Access токен пользователя")
    private String accessToken;
    @Schema(description = "Время истечения срока действия access токена")
    private LocalDateTime expiresAtAccess;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessTokenResponseDto that = (AccessTokenResponseDto) o;
        return Objects.equals(email, that.email) && Objects.equals(accessToken, that.accessToken) && Objects.equals(expiresAtAccess, that.expiresAtAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, accessToken, expiresAtAccess);
    }
}
