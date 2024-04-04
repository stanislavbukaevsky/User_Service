package com.github.stanislavbukaevsky.userservice.exception;

/**
 * Класс-исключение, если произошла ошибка с логином пользователя.
 * Наследуется от класса {@link RuntimeException}
 */
public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
