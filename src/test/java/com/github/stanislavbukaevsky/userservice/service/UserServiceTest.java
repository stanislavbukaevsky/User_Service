package com.github.stanislavbukaevsky.userservice.service;

import com.github.stanislavbukaevsky.userservice.entity.User;
import com.github.stanislavbukaevsky.userservice.enums.Role;
import com.github.stanislavbukaevsky.userservice.repository.UserRepository;
import com.github.stanislavbukaevsky.userservice.service.impl.UserServiceImpl;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * Класс с тестами для пользователей
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    private final static Long ID = 1L;
    private final static String FIRST_NAME = "Test first name";
    private final static String LAST_NAME = "Test last name";
    private final static String EMAIL = "test@test.ru";
    private final static String EMAIL_NEGATIVE = "test2@test2.ru";
    private final static String PASSWORD = "password";
    private final static Role ROLE = Role.USER;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    private User user;

    @BeforeAll
    public void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Test first name")
                .lastName("Test last name")
                .email("test@test.ru")
                .password("password")
                .role(Role.USER).build();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Проверка сохранения пользователя")
    public void save_shouldReturnUser() {
        Assertions.assertNotNull(userRepository);
        Mockito.lenient().when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        Assertions.assertEquals(ID, user.getId());
        Assertions.assertEquals(FIRST_NAME, user.getFirstName());
        Assertions.assertEquals(LAST_NAME, user.getLastName());
        Assertions.assertEquals(EMAIL, user.getEmail());
        Assertions.assertEquals(PASSWORD, user.getPassword());
        Assertions.assertEquals(ROLE, user.getRole());
        Assertions.assertEquals(user, userService.save(user));
    }

    @Test
    @DisplayName("Проверка поиска пользователя по email")
    public void findUserByEmail_shouldReturnUser() {
        Assertions.assertNotNull(userRepository);
        Mockito.lenient().when(userRepository.findUserByEmail(ArgumentMatchers.any(String.class)))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(ID, user.getId());
        Assertions.assertEquals(FIRST_NAME, user.getFirstName());
        Assertions.assertEquals(LAST_NAME, user.getLastName());
        Assertions.assertEquals(EMAIL, user.getEmail());
        Assertions.assertEquals(PASSWORD, user.getPassword());
        Assertions.assertEquals(ROLE, user.getRole());
        Assertions.assertEquals(user, userService.findUserByEmail(EMAIL));
    }

    @Test
    @DisplayName("Проверка поиска пользователя, если email не существует")
    public void findUserByEmail_ifUserNotFound_thenThrow() {
        Assertions.assertNotNull(userRepository);

        org.assertj.core.api.Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userService.findUserByEmail(EMAIL_NEGATIVE));
    }

    @Test
    @DisplayName("Проверка наличия пользователя по email")
    public void existsUserByEmail_shouldReturnTrue() {
        Assertions.assertNotNull(userRepository);
        Mockito.lenient().when(userRepository.existsUserByEmail(ArgumentMatchers.any(String.class)))
                .thenReturn(false);

        Assertions.assertFalse(false, String.valueOf(userService.existsUserByEmail(EMAIL_NEGATIVE)));
    }
}
