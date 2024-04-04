package com.github.stanislavbukaevsky.userservice.service;

import com.github.stanislavbukaevsky.userservice.entity.User;

/**
 * Сервис-интерфейс для работы со всеми пользователями зарегистрированными на платформе.
 * В этом интерфейсе прописана только сигнатура методов без реализации
 */
public interface UserService {
    User save(User user);
    User findUserByEmail(String email);
    Boolean existsUserByEmail(String email);
}
