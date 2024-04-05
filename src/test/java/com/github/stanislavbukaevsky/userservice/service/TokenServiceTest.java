package com.github.stanislavbukaevsky.userservice.service;

import com.github.stanislavbukaevsky.userservice.dto.AccessTokenRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.AccessTokenResponseDto;
import com.github.stanislavbukaevsky.userservice.entity.Token;
import com.github.stanislavbukaevsky.userservice.entity.User;
import com.github.stanislavbukaevsky.userservice.enums.Role;
import com.github.stanislavbukaevsky.userservice.exception.AuthenticationUsersException;
import com.github.stanislavbukaevsky.userservice.mapper.TokenMapper;
import com.github.stanislavbukaevsky.userservice.repository.TokenRepository;
import com.github.stanislavbukaevsky.userservice.security.UserSecurityService;
import com.github.stanislavbukaevsky.userservice.security.impl.UserSecurity;
import com.github.stanislavbukaevsky.userservice.service.impl.TokenServiceImpl;
import com.github.stanislavbukaevsky.userservice.token.TokenDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс с тестами для токена
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokenServiceTest {
    private final static Long ID = 10L;
    private final static String REFRESH_TOKEN = "test refresh token";
    private final static Date DATE_EXPIRATION = new Date();
    private final static Role ROLE = Role.USER;
    private final static String EMAIL = "test@test.ru";
    @InjectMocks
    private TokenServiceImpl tokenService;
    @Mock
    private TokenDetailsService tokenDetailsService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private TokenMapper tokenMapper;
    @Mock
    private UserSecurityService userSecurityService;
    @Mock
    private UserService userService;
    private User user;
    private Token token;
    private AccessTokenRequestDto request;

    @BeforeAll
    public void setUp() {
        token = Token.builder()
                .id(10L)
                .refreshToken("test refresh token")
                .dateTimeCreation(LocalDateTime.now())
                .dateTimeExpires(LocalDateTime.now())
                .user(user).build();
        user = User.builder()
                .firstName("Test first name")
                .lastName("Test last name")
                .email("test@test.ru")
                .password("password")
                .role(Role.USER)
                .token(token).build();
        request = AccessTokenRequestDto.builder()
                .refreshToken(REFRESH_TOKEN).build();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        tokenService = new TokenServiceImpl(
                tokenDetailsService,
                tokenRepository,
                tokenMapper,
                userSecurityService,
                userService);
    }

    @Test
    @DisplayName("Проверка сохранения токена")
    public void addToken_shouldReturnUser() {
        Assertions.assertNotNull(tokenRepository);
        Mockito.lenient().when(tokenRepository.save(ArgumentMatchers.any(Token.class))).thenReturn(token);
        Mockito.lenient().when(tokenDetailsService.getRefreshExpiration()).thenReturn(DATE_EXPIRATION);

        Assertions.assertEquals(ID, token.getId());
        Assertions.assertEquals(REFRESH_TOKEN, token.getRefreshToken());
        Assertions.assertEquals(token, tokenService.addToken(REFRESH_TOKEN, user, token));
    }

    @Test
    @DisplayName("Проверка на выдачу нового токена")
    public void replaceAccessToken_shouldReturnUser() {
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUser(user);
        Map<String, Object> map = new HashMap<>();
        map.put("role", ROLE);
        map.put("email", EMAIL);
        Claims claims = new DefaultClaims(map);

        Assertions.assertNotNull(userService);
        Mockito.lenient().when(tokenDetailsService.validateRefreshToken(ArgumentMatchers.any()))
                .thenReturn(true);
        Mockito.lenient().when(tokenDetailsService.getRefreshClaims(ArgumentMatchers.any()))
                .thenReturn(claims);
        Mockito.lenient().when(userService.findUserByEmail(ArgumentMatchers.any()))
                .thenReturn(user);
        Mockito.lenient().when(userSecurityService.loadUserByUsername(ArgumentMatchers.any()))
                .thenReturn(userSecurity);
        Mockito.lenient().when(tokenDetailsService.getAccessExpiration()).thenReturn(DATE_EXPIRATION);

        AccessTokenResponseDto actual = tokenService.replaceAccessToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Проверка на выдачу нового токена, если он не валиден")
    public void replaceAccessToken_ifTokenNotFound_thenThrow() {
        Assertions.assertNotNull(tokenDetailsService);
        Mockito.lenient().when(tokenDetailsService.validateRefreshToken(ArgumentMatchers.any()))
                .thenReturn(false);

        org.assertj.core.api.Assertions.assertThatExceptionOfType(AuthenticationUsersException.class)
                .isThrownBy(() -> tokenService.replaceAccessToken(request));
    }
}
