package com.github.stanislavbukaevsky.userservice.constant;

/**
 * Этот класс содержит текстовые константные переменные для всех логов в приложении
 */
public class LoggerTextMessageConstant {
    // Логи для методов в сервисах
    public static final String SAVE_USER_MESSAGE_LOGGER_SERVICE = "Вызван метод сохранения пользователя в базе данных. Личные данные пользователя: {}";
    public static final String FIND_USER_BY_EMAIL_MESSAGE_LOGGER_SERVICE = "Вызван метод поиска пользователя по его электронной почте. Электронная почта пользователя: {}";
    public static final String EXISTS_USER_BY_EMAIL_MESSAGE_LOGGER_SERVICE = "Вызван метод проверки электронной почты в базе данных. Электронная почта пользователя: {}";
    public static final String ADD_TOKEN_MESSAGE_LOGGER_SERVICE = "Вызван метод для добавления в базу данных нового refresh-токена";
    public static final String REPLACE_ACCESS_TOKEN_MESSAGE_LOGGER_SERVICE = "Вызван метод для генерации нового access-токена для зарегистрированных пользователей. Refresh-токен пользователя: {}";
    public static final String REGISTRATION_MESSAGE_LOGGER_SERVICE = "Вызван метод для регистрации нового пользователя. Зарегистрированный пользователь: {}";
    public static final String AUTHENTICATION_MESSAGE_LOGGER_SERVICE = "Вызван метод для аутентификации зарегистрированного пользователя. Электронная почта пользователя: {}";
    public static final String AUTHENTICATION_MESSAGE_ERROR_LOGGER_SERVICE = "Введен неправильный логин или пароль! Проверьте правильность введенных данных.";

    // Логи для пакета security
    public static final String GET_USERNAME_MESSAGE_LOGGER_SECURITY = "Вызван метод для получения логина пользователя. Логин пользователя: {}";
    public static final String GET_PASSWORD_MESSAGE_LOGGER_SECURITY = "Вызван метод для получения пароля пользователя. Пароль пользователя: {}";
    public static final String GET_AUTHORITIES_MESSAGE_LOGGER_SECURITY = "Вызван метод для получения роли пользователя. Роль пользователя: {}";
    public static final String LOAD_USER_BY_USERNAME_MESSAGE_LOGGER_SECURITY = "Вызван метод для поиска пользователя по его уникальному логину. Логин пользователя: {}";
    public static final String PASSWORD_ENCODER_ENCODE_MESSAGE_LOGGER_SERVICE = "Вызван метод шифрования пароля пользователя. Пароль пользователя: {}";
    public static final String PASSWORD_ENCODER_MATCHES_MESSAGE_LOGGER_SERVICE = "Вызван метод проверки пароля пользователя. Полученный пароль от пользователя: {}. Зашифрованный пароль, сохраненный в базе данных: {}";
}
