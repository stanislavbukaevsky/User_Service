package com.github.stanislavbukaevsky.userservice.service.impl;

import com.github.stanislavbukaevsky.userservice.dto.AuthenticationRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.AuthenticationResponseDto;
import com.github.stanislavbukaevsky.userservice.dto.RegistrationRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.RegistrationResponseDto;
import com.github.stanislavbukaevsky.userservice.entity.Token;
import com.github.stanislavbukaevsky.userservice.entity.User;
import com.github.stanislavbukaevsky.userservice.enums.Role;
import com.github.stanislavbukaevsky.userservice.exception.AuthenticationUsersException;
import com.github.stanislavbukaevsky.userservice.exception.LoginAlreadyExistsException;
import com.github.stanislavbukaevsky.userservice.mapper.TokenMapper;
import com.github.stanislavbukaevsky.userservice.mapper.UserMapper;
import com.github.stanislavbukaevsky.userservice.repository.TokenRepository;
import com.github.stanislavbukaevsky.userservice.security.UserSecurityService;
import com.github.stanislavbukaevsky.userservice.security.impl.UserSecurity;
import com.github.stanislavbukaevsky.userservice.service.AuthService;
import com.github.stanislavbukaevsky.userservice.service.TokenService;
import com.github.stanislavbukaevsky.userservice.service.UserService;
import com.github.stanislavbukaevsky.userservice.token.TokenDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.AUTHENTICATION_USERS_MESSAGE_EXCEPTION_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.REGISTRATION_MESSAGE_EXCEPTION_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.AUTHENTICATION_MESSAGE_ERROR_LOGGER_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.AUTHENTICATION_MESSAGE_LOGGER_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.REGISTRATION_MESSAGE_LOGGER_SERVICE;

/**
 * Сервис-класс с бизнес-логикой для регистрации и аутентификации пользователей на платформе.
 * Реализует интерфейс {@link AuthService}
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthServiceImpl implements AuthService {
    private final TokenDetailsService tokenDetailsService;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final TokenMapper tokenMapper;
    private final UserSecurityService userSecurityService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Реализация метода регистрации новых пользователей на платформе
     *
     * @param request класс-DTO для регистрации пользователя на платформе
     * @return Возвращает DTO с информацией о зарегистрированном пользователе
     */
    @Override
    public RegistrationResponseDto registration(@Valid RegistrationRequestDto request) {
        Boolean checkUser = userService.existsUserByEmail(request.getEmail());

        if (checkUser) {
            throw new LoginAlreadyExistsException(REGISTRATION_MESSAGE_EXCEPTION_SERVICE);
        } else {
            User user = userMapper.mappingEntityUser(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);
            User result = userService.save(user);
            log.info(REGISTRATION_MESSAGE_LOGGER_SERVICE, result);
            return userMapper.mappingRegistrationResponseDto(result);
        }
    }

    /**
     * Реализация метода аутентификации пользователей на платформе
     *
     * @param request класс-DTO для аутентификации пользователя на платформе
     * @return Возвращает DTO с информацией об аутентифицированном пользователе на платформе
     */
    @Override
    public AuthenticationResponseDto authentication(@Valid AuthenticationRequestDto request) {
        UserSecurity userSecurity = (UserSecurity) userSecurityService.loadUserByUsername(request.getEmail());

        if (passwordEncoder.matches(request.getPassword(), userSecurity.getPassword())) {
            log.info(AUTHENTICATION_MESSAGE_LOGGER_SERVICE, request.getEmail());
            return getGeneratingAuthenticationResponse(userSecurity);
        } else {
            log.error(AUTHENTICATION_MESSAGE_ERROR_LOGGER_SERVICE);
            throw new AuthenticationUsersException(AUTHENTICATION_USERS_MESSAGE_EXCEPTION_SERVICE);
        }
    }

    private AuthenticationResponseDto getGeneratingAuthenticationResponse(UserSecurity userSecurity) {
        final Long id = userSecurity.getUser().getId();
        final String firstName = userSecurity.getUser().getFirstName();
        final String lastName = userSecurity.getUser().getLastName();
        final String email = userSecurity.getUser().getEmail();
        final String password = userSecurity.getUser().getPassword();
        final String role = userSecurity.getUser().getRole().name();
        final String accessToken = tokenDetailsService.generateAccessToken(userSecurity);
        final String refreshToken = tokenDetailsService.generateRefreshToken(userSecurity);
        final LocalDateTime dateTimeCreation = LocalDateTime.now();
        final LocalDateTime dateTimeExpiresAccess = LocalDateTime.ofInstant(
                tokenDetailsService.getAccessExpiration().toInstant(), ZoneId.systemDefault());
        final LocalDateTime dateTimeExpiresRefresh = LocalDateTime.ofInstant(
                        tokenDetailsService.getRefreshExpiration().toInstant(), ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.MINUTES);
        Token token = tokenRepository.findTokenByUserId(id).orElseGet(() ->
                tokenService.addToken(refreshToken, userSecurity.getUser(), new Token()));
        if (token.getUser().getId().equals(id)) {
            token.setRefreshToken(refreshToken);
            token.setDateTimeCreation(dateTimeCreation);
            token.setDateTimeExpires(dateTimeExpiresRefresh);
            tokenRepository.save(token);
        }

        return tokenMapper.mappingAuthenticationResponse(
                id,
                firstName,
                lastName,
                email,
                password,
                role,
                accessToken,
                refreshToken,
                dateTimeExpiresAccess,
                dateTimeExpiresRefresh);
    }
}
