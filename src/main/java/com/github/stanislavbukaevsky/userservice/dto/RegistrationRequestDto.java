package com.github.stanislavbukaevsky.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Класс-DTO для запроса от пользователя на регистрацию
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "Объект регистрации для запроса от пользователя")
public class RegistrationRequestDto {
    @NotEmpty(message = "Поле имени не должно быть пустым!")
    @Size(min = 2, max = 64, message = "Имя должно содержать от 2 до 64 символов!")
    @Schema(description = "Имя пользователя")
    private String firstName;
    @NotEmpty(message = "Поле фамилии не должно быть пустым!")
    @Size(min = 2, max = 64, message = "Фамилия должна содержать от 2 до 64 символов!")
    @Schema(description = "Фамилия пользователя")
    private String lastName;
    @NotEmpty(message = "Поле электронной почты не должно быть пустым!")
    @Size(min = 6, max = 48, message = "Электронная почта должна содержать от 6 до 48 символов!")
    @Email(message = "Электронная почта должна быть следующего вида: test@test.ru")
    @Schema(description = "Электронная почта пользователя при регистрации, аутентификации и авторизации")
    private String email;
    @NotEmpty(message = "Поле пароля не должно быть пустым!")
    @Size(min = 6, max = 1000, message = "Пароль должен содержать от 6 до 1000 символов!")
    @Schema(description = "Пароль пользователя")
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationRequestDto that = (RegistrationRequestDto) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password);
    }
}
