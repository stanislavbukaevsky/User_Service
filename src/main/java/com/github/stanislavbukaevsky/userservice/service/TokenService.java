package com.github.stanislavbukaevsky.userservice.service;

import com.github.stanislavbukaevsky.userservice.dto.AccessTokenRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.AccessTokenResponseDto;
import com.github.stanislavbukaevsky.userservice.entity.Token;
import com.github.stanislavbukaevsky.userservice.entity.User;
import jakarta.validation.Valid;

/**
 * Сервис-интерфейс для всех refresh-токенов, выданных на платформе.
 * В этом интерфейсе прописана только сигнатура методов без реализации
 */
public interface TokenService {
    Token addToken(String refreshToken, User user, Token token);
    AccessTokenResponseDto replaceAccessToken(@Valid AccessTokenRequestDto request);
}
