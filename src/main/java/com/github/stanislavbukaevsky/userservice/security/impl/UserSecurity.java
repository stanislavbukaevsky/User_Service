package com.github.stanislavbukaevsky.userservice.security.impl;

import com.github.stanislavbukaevsky.userservice.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Collection;
import java.util.List;

import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.GET_AUTHORITIES_MESSAGE_LOGGER_SECURITY;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.GET_PASSWORD_MESSAGE_LOGGER_SECURITY;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.GET_USERNAME_MESSAGE_LOGGER_SECURITY;

/**
 * Этот класс с бизнес-логикой предоставляет всю необходимую информацию для построения объекта аутентификации.
 * Здесь находятся методы, которые достают данные пользователя из базы данных.
 * Реализует методы интерфейса {@link UserDetails}
 */
@RequestScope
@Getter
@Setter
@Component
@RequiredArgsConstructor
@Slf4j
public class UserSecurity implements UserDetails {
    private User user;

    /**
     * Метод для получения email пользователя
     *
     * @return Возвращает email пользователя
     */
    @Override
    public String getUsername() {
        log.info(GET_USERNAME_MESSAGE_LOGGER_SECURITY, user.getEmail());
        return user.getEmail();
    }

    /**
     * Метод для получения пароля пользователя
     *
     * @return Возвращает пароль пользователя
     */
    @Override
    public String getPassword() {
        log.info(GET_PASSWORD_MESSAGE_LOGGER_SECURITY, user.getPassword());
        return user.getPassword();
    }

    /**
     * Метод для получении коллекции ролей пользователей
     *
     * @return Возвращает список ролей пользователей
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        log.info(GET_AUTHORITIES_MESSAGE_LOGGER_SECURITY, user.getRole().name());
        return authorities;
    }

    /**
     * Метод об истечении срока действия учетной записи пользователя
     *
     * @return Возвращает true, если у учетной записи пользователя не истек срок действия
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Метод о блокировки учетной записи пользователя
     *
     * @return Возвращает true, если учетная запись пользователя не заблокирована
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Метод об истечении срока действия учетных данных пользователя
     *
     * @return Возвращает true, если у учетных данных пользователя не истек срок действия
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Метод об активности пользователя
     *
     * @return Возвращает true, если пользователь активен
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
