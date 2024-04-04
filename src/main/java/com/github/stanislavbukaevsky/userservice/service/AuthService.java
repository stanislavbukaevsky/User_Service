package com.github.stanislavbukaevsky.userservice.service;

import com.github.stanislavbukaevsky.userservice.dto.AuthenticationRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.AuthenticationResponseDto;
import com.github.stanislavbukaevsky.userservice.dto.RegistrationRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.RegistrationResponseDto;
import jakarta.validation.Valid;

/**
 * Сервис-интерфейс для регистрации и аутентификации пользователей на платформе.
 * В этом интерфейсе прописана только сигнатура методов без реализации
 */
public interface AuthService {
    RegistrationResponseDto registration(@Valid RegistrationRequestDto request);
    AuthenticationResponseDto authentication(@Valid AuthenticationRequestDto request);
}
