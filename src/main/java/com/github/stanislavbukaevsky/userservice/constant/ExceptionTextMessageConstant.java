package com.github.stanislavbukaevsky.userservice.constant;

/**
 * Этот класс содержит текстовые константные переменные для всех исключений в приложении
 */
public class ExceptionTextMessageConstant {
    public static final String USER_NOT_FOUND_EXCEPTION = "Пользователя с таким логином не существует в базе данных. Попробуйте ввести другой логин.";
    public static final String EXPIRED_JWT_EXCEPTION_MESSAGE_SERVICE = "Срок действия токена истек! Получите новый JWT токен и повторите попытку";
    public static final String UNSUPPORTED_JWT_EXCEPTION_MESSAGE_SERVICE = "Неподдерживаемый токен! Получите новый JWT токен и повторите попытку";
    public static final String MALFORMED_JWT_EXCEPTION_MESSAGE_SERVICE = "Токен неправильно сформирован! Получите новый JWT токен и повторите попытку";
    public static final String SIGNATURE_EXCEPTION_MESSAGE_SERVICE = "Подпись токена не действительна! Получите новый JWT токен и повторите попытку";
    public static final String EXCEPTION_MESSAGE_SERVICE = "Недопустимый токен! Получите новый JWT токен и повторите попытку";
    public static final String REPLACE_ACCESS_AND_REFRESH_TOKEN_MESSAGE_EXCEPTION_SERVICE = "Вы ввели невалидный refresh-токен! Введите актуальный токен для выполнения операции.";
    public static final String REGISTRATION_MESSAGE_EXCEPTION_SERVICE = "Пользователь с таким логином уже существует! Попробуйте выбрать другой логин для регистрации";
    public static final String AUTHENTICATION_USERS_MESSAGE_EXCEPTION_SERVICE = "Вы ввели неправильный логин или пароль! Попробуйте ввести другую комбинацию логина и пароля.";
}
