package com.github.stanislavbukaevsky.userservice.mapper;

import com.github.stanislavbukaevsky.userservice.dto.AccessTokenResponseDto;
import com.github.stanislavbukaevsky.userservice.dto.AuthenticationResponseDto;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

/**
 * Маппер-интерфейс, который преобразует полученную информацию о токене в DTO
 */
@Mapper
public interface TokenMapper {
    AuthenticationResponseDto mappingAuthenticationResponse(Long id,
                                                       String firstName,
                                                       String lastName,
                                                       String email,
                                                       String password,
                                                       String role,
                                                       String accessToken,
                                                       String refreshToken,
                                                       LocalDateTime expiresAtAccess,
                                                       LocalDateTime expiresAtRefresh);
    AccessTokenResponseDto mappingAccessTokenResponse(String email, String accessToken, LocalDateTime expiresAtAccess);
}
