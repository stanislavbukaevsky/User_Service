package com.github.stanislavbukaevsky.userservice.service;

import com.github.stanislavbukaevsky.userservice.dto.AuthenticationRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.AuthenticationResponseDto;
import com.github.stanislavbukaevsky.userservice.dto.RegistrationRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.RegistrationResponseDto;
import com.github.stanislavbukaevsky.userservice.entity.Token;
import com.github.stanislavbukaevsky.userservice.entity.User;
import com.github.stanislavbukaevsky.userservice.enums.Role;
import com.github.stanislavbukaevsky.userservice.exception.LoginAlreadyExistsException;
import com.github.stanislavbukaevsky.userservice.mapper.TokenMapper;
import com.github.stanislavbukaevsky.userservice.mapper.UserMapper;
import com.github.stanislavbukaevsky.userservice.repository.TokenRepository;
import com.github.stanislavbukaevsky.userservice.security.UserSecurityService;
import com.github.stanislavbukaevsky.userservice.security.impl.UserSecurity;
import com.github.stanislavbukaevsky.userservice.service.impl.AuthServiceImpl;
import com.github.stanislavbukaevsky.userservice.token.TokenDetailsService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * Класс с тестами для аутентификации
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTest {
    private final static Long ID = 1L;
    private final static String FIRST_NAME = "Test first name";
    private final static String LAST_NAME = "Test last name";
    private final static String EMAIL = "test@test.ru";
    private final static String PASSWORD = "password";
    private final static String ENCODE_PASSWORD = "password";
    private final static Role ROLE = Role.USER;
    private final static Long ID_TOKEN = 10L;
    private final static String REFRESH_TOKEN = "test refresh token";
    private final static Date DATE_EXPIRATION = new Date();
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenDetailsService tokenDetailsService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private TokenService tokenService;
    @Mock
    private TokenMapper tokenMapper;
    @Mock
    private UserSecurityService userSecurityService;
    private RegistrationRequestDto regRequest;
    private RegistrationResponseDto regResponse;
    private AuthenticationRequestDto authRequest;
    private User user;
    private Token token;

    @BeforeAll
    public void setUp() {
        regRequest = RegistrationRequestDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD).build();
        regResponse = RegistrationResponseDto.builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(ROLE.name()).build();
        authRequest = AuthenticationRequestDto.builder()
                .email(EMAIL)
                .password(PASSWORD).build();
        user = User.builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(Role.USER).build();
        token = Token.builder()
                .id(ID_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .dateTimeCreation(LocalDateTime.now())
                .dateTimeExpires(LocalDateTime.now())
                .user(user).build();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(tokenDetailsService,
                tokenRepository, tokenService,
                tokenMapper, userSecurityService, userService,
                userMapper, passwordEncoder);
    }

    @Test
    @DisplayName("Проверка регистрации пользователя")
    public void registration_shouldReturnResponse() {
        Assertions.assertNotNull(passwordEncoder);
        Mockito.lenient().when(userService.existsUserByEmail(ArgumentMatchers.any(String.class)))
                .thenReturn(false);
        Mockito.lenient().when(userMapper.mappingEntityUser(ArgumentMatchers.any()))
                .thenReturn(user);
        Mockito.lenient().when(passwordEncoder.encode(ArgumentMatchers.any(String.class)))
                .thenReturn(ENCODE_PASSWORD);
        Mockito.lenient().when(userService.save(ArgumentMatchers.any(User.class)))
                .thenReturn(user);
        Mockito.lenient().when(userMapper.mappingRegistrationResponseDto(ArgumentMatchers.any(User.class)))
                .thenReturn(regResponse);
        org.assertj.core.api.Assertions.assertThat(regResponse).isEqualTo(authService.registration(regRequest));

        RegistrationResponseDto actual = authService.registration(regRequest);
        org.assertj.core.api.Assertions.assertThat(actual.getId()).isEqualTo(user.getId());
        org.assertj.core.api.Assertions.assertThat(actual.getFirstName()).isEqualTo(user.getFirstName());
        org.assertj.core.api.Assertions.assertThat(actual.getLastName()).isEqualTo(user.getLastName());
        org.assertj.core.api.Assertions.assertThat(actual.getEmail()).isEqualTo(user.getEmail());
        org.assertj.core.api.Assertions.assertThat(actual.getPassword()).isEqualTo(user.getPassword());
        org.assertj.core.api.Assertions.assertThat(actual.getRole()).isEqualTo(user.getRole().name());
    }

    @Test
    @DisplayName("Проверка регистрации пользователя, если его email уже существует")
    public void registration_ifUserExists_thenThrow() {
        Assertions.assertNotNull(userService);
        Mockito.lenient().when(userService.existsUserByEmail(ArgumentMatchers.any(String.class)))
                .thenReturn(true);

        org.assertj.core.api.Assertions.assertThatExceptionOfType(LoginAlreadyExistsException.class)
                .isThrownBy(() -> authService.registration(regRequest));
    }

    @Test
    @DisplayName("Проверка аутентификации пользователя")
    public void authentication_shouldReturnResponse() {
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUser(user);

        Assertions.assertNotNull(passwordEncoder);
        Mockito.lenient().when(userSecurityService.loadUserByUsername(ArgumentMatchers.any()))
                .thenReturn(userSecurity);
        Mockito.lenient().when(passwordEncoder.matches(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(true);
        Mockito.lenient().when(tokenDetailsService.getAccessExpiration()).thenReturn(DATE_EXPIRATION);
        Mockito.lenient().when(tokenDetailsService.getRefreshExpiration()).thenReturn(DATE_EXPIRATION);
        Mockito.lenient().when(tokenRepository.findTokenByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(token));
        Mockito.lenient().when(tokenService.addToken(
                ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(token);

        AuthenticationResponseDto actual = authService.authentication(authRequest);
        Assertions.assertEquals(token.getUser().getId(), ID);
        Assertions.assertNull(actual);
    }
}
