package com.github.stanislavbukaevsky.userservice.service.impl;

import com.github.stanislavbukaevsky.userservice.dto.AccessTokenRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.AccessTokenResponseDto;
import com.github.stanislavbukaevsky.userservice.entity.Token;
import com.github.stanislavbukaevsky.userservice.entity.User;
import com.github.stanislavbukaevsky.userservice.exception.AuthenticationUsersException;
import com.github.stanislavbukaevsky.userservice.mapper.TokenMapper;
import com.github.stanislavbukaevsky.userservice.repository.TokenRepository;
import com.github.stanislavbukaevsky.userservice.security.UserSecurityService;
import com.github.stanislavbukaevsky.userservice.security.impl.UserSecurity;
import com.github.stanislavbukaevsky.userservice.service.TokenService;
import com.github.stanislavbukaevsky.userservice.service.UserService;
import com.github.stanislavbukaevsky.userservice.token.TokenDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.REPLACE_ACCESS_AND_REFRESH_TOKEN_MESSAGE_EXCEPTION_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.ADD_TOKEN_MESSAGE_LOGGER_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.REPLACE_ACCESS_TOKEN_MESSAGE_LOGGER_SERVICE;

/**
 * Сервис-класс с бизнес-логикой всех refresh-токенов, выданных на платформе.
 * Реализует интерфейс {@link TokenService}
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class TokenServiceImpl implements TokenService {
    private final TokenDetailsService tokenDetailsService;
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;
    private final UserSecurityService userSecurityService;
    private final UserService userService;

    /**
     * Реализация метода для добавления нового refresh-токена в базу данных
     *
     * @param refreshToken refresh-токен
     * @param user         сущность пользователя
     * @param token        сущность токена
     * @return Возвращает добавленный refresh-токен
     */
    @Override
    public Token addToken(String refreshToken, User user, Token token) {
        LocalDateTime dateTimeCreation = LocalDateTime.now();
        LocalDateTime dateTimeExpires = LocalDateTime.ofInstant(
                        tokenDetailsService.getRefreshExpiration().toInstant(), ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.MINUTES);
        token.setRefreshToken(refreshToken);
        token.setDateTimeCreation(dateTimeCreation);
        token.setDateTimeExpires(dateTimeExpires);
        token.setUser(user);
        log.info(ADD_TOKEN_MESSAGE_LOGGER_SERVICE);
        return tokenRepository.save(token);
    }

    /**
     * Реализация метода для выдачи нового access-токена для зарегистрированного пользователя на платформе
     *
     * @param request запрос от пользователя
     * @return Возвращает ответ с личной информацией о пользователе с новым, сгенерированным access-токеном
     */
    @Override
    public AccessTokenResponseDto replaceAccessToken(@Valid AccessTokenRequestDto request) {
        if (tokenDetailsService.validateRefreshToken(request.getRefreshToken())) {
            final Claims claims = tokenDetailsService.getRefreshClaims(request.getRefreshToken());
            final String email = claims.getSubject();
            final String savedRefreshToken = findRefreshTokenByEmail(email);
            if (savedRefreshToken != null && savedRefreshToken.equals(request.getRefreshToken())) {
                UserSecurity userSecurity = (UserSecurity) userSecurityService.loadUserByUsername(email);
                final String emailUser = userSecurity.getUser().getEmail();
                final String accessToken = tokenDetailsService.generateAccessToken(userSecurity);
                final LocalDateTime dateTimeExpiresAccess = LocalDateTime.ofInstant(
                        tokenDetailsService.getAccessExpiration().toInstant(), ZoneId.systemDefault());
                log.info(REPLACE_ACCESS_TOKEN_MESSAGE_LOGGER_SERVICE, request.getRefreshToken());
                return tokenMapper.mappingAccessTokenResponse(emailUser, accessToken, dateTimeExpiresAccess);
            }
        }
        throw new AuthenticationUsersException(REPLACE_ACCESS_AND_REFRESH_TOKEN_MESSAGE_EXCEPTION_SERVICE);
    }

    private String findRefreshTokenByEmail(String email) {
        User user = userService.findUserByEmail(email);
        return user.getToken().getRefreshToken();
    }
}
