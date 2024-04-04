package com.github.stanislavbukaevsky.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс-сущность для всех refresh-токенов
 */
@Entity
@Table(name = Token.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class Token {
    public static final String TABLE_NAME = "token_users";
    public static final String ID = "id";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String DATE_TIME_CREATION = "date_time_creation";
    public static final String DATE_TIME_EXPIRES = "date_time_expires";
    public static final String USER_ID = "user_id";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Token.ID)
    private Long id;
    @Column(name = Token.REFRESH_TOKEN)
    private String refreshToken;
    @Column(name = Token.DATE_TIME_CREATION)
    private LocalDateTime dateTimeCreation;
    @Column(name = Token.DATE_TIME_EXPIRES)
    private LocalDateTime dateTimeExpires;
    @OneToOne
    @JoinColumn(name = Token.USER_ID)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(id, token.id) && Objects.equals(refreshToken, token.refreshToken) && Objects.equals(dateTimeCreation, token.dateTimeCreation) && Objects.equals(dateTimeExpires, token.dateTimeExpires) && Objects.equals(user, token.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, refreshToken, dateTimeCreation, dateTimeExpires, user);
    }
}
