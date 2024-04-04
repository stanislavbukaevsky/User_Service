package com.github.stanislavbukaevsky.userservice.service.impl;

import com.github.stanislavbukaevsky.userservice.entity.User;
import com.github.stanislavbukaevsky.userservice.repository.UserRepository;
import com.github.stanislavbukaevsky.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.USER_NOT_FOUND_EXCEPTION;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.EXISTS_USER_BY_EMAIL_MESSAGE_LOGGER_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.FIND_USER_BY_EMAIL_MESSAGE_LOGGER_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.SAVE_USER_MESSAGE_LOGGER_SERVICE;

/**
 * Сервис-класс с бизнес-логикой для всех пользователей зарегистрированных на платформе.
 * Реализует интерфейс {@link UserService}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Реализация метода для сохранения пользователей в базе данных
     *
     * @param user объект пользователя
     * @return Возвращает сохраненного пользователя в базе данных
     */
    @Override
    public User save(User user) {
        log.info(SAVE_USER_MESSAGE_LOGGER_SERVICE, user);
        return userRepository.save(user);
    }

    /**
     * Реализация метода для поиска пользователей по его электронной почте
     *
     * @param email электронная почта пользователя
     * @return Возвращает найденного пользователя по его электронной почте
     */
    @Override
    public User findUserByEmail(String email) {
        log.info(FIND_USER_BY_EMAIL_MESSAGE_LOGGER_SERVICE, email);
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(USER_NOT_FOUND_EXCEPTION));
    }

    /**
     * Реализация метода для проверки, есть ли уже электронная почта в базе данных
     *
     * @param email электронная почта пользователя
     * @return Возвращает true, если пользователь уже существует в базе данных или false, если его нет
     */
    @Override
    public Boolean existsUserByEmail(String email) {
        log.info(EXISTS_USER_BY_EMAIL_MESSAGE_LOGGER_SERVICE, email);
        return userRepository.existsUserByEmail(email);
    }
}
