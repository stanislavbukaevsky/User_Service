package com.github.stanislavbukaevsky.userservice.entity;

import com.github.stanislavbukaevsky.userservice.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Класс-сущность для всех пользователей
 */
@Entity
@Table(name = User.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class User {
    public static final String TABLE_NAME = "user_profile";
    public static final String ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";
    public static final String TOKEN_MAPPED = "user";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = User.ID)
    private Long id;
    @Column(name = User.FIRST_NAME)
    private String firstName;
    @Column(name = User.LAST_NAME)
    private String lastName;
    @Column(name = User.EMAIL)
    private String email;
    @Column(name = User.PASSWORD)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = User.ROLE)
    private Role role;
    @OneToOne(mappedBy = User.TOKEN_MAPPED, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Token token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && role == user.role && Objects.equals(token, user.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, role, token);
    }
}
