package com.github.stanislavbukaevsky.userservice.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Класс-исключение для аутентификации пользователей.
 * Наследуется от класса {@link AuthenticationException}
 */
public class AuthenticationUsersException extends AuthenticationException {
    public AuthenticationUsersException(String msg) {
        super(msg);
    }
}
