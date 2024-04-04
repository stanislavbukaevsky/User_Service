package com.github.stanislavbukaevsky.userservice.token;

import com.github.stanislavbukaevsky.userservice.security.impl.UserSecurity;
import io.jsonwebtoken.Claims;
import lombok.NonNull;

import java.util.Date;

/**
 * Интерфейс для генерации и валидации access и refresh токенов
 */
public interface TokenDetailsService {
    String generateAccessToken(@NonNull UserSecurity userSecurity);
    String generateRefreshToken(@NonNull UserSecurity userSecurity);
    Date getAccessExpiration();
    Date getRefreshExpiration();
    boolean validateAccessToken(@NonNull String accessToken);
    boolean validateRefreshToken(@NonNull String refreshToken);
    Claims getAccessClaims(@NonNull String accessToken);
    Claims getRefreshClaims(@NonNull String refreshToken);
}
