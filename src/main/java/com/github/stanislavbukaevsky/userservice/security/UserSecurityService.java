package com.github.stanislavbukaevsky.userservice.security;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Интерфейс для поиска пользователей в базе данных.
 * Наследуется от интерфейса {@link UserDetailsService}
 */
public interface UserSecurityService extends UserDetailsService {
}
