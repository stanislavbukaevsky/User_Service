package com.github.stanislavbukaevsky.userservice.repository;

import com.github.stanislavbukaevsky.userservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс-репозиторий для работы с методами всех refresh-токенов, выданных на платформе.
 * Наследуется от интерфейса {@link JpaRepository}. Параметры:
 * {@link Token} - класс-сущность
 * {@link Long} - идентификатор
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTokenByUserId(Long id);
}
