package com.github.stanislavbukaevsky.userservice.repository;

import com.github.stanislavbukaevsky.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс-репозиторий для работы с методами всех пользователей приложения.
 * Наследуется от интерфейса {@link JpaRepository}. Параметры:
 * {@link User} - класс-сущность
 * {@link Long} - идентификатор
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Boolean existsUserByEmail(String email);
}
