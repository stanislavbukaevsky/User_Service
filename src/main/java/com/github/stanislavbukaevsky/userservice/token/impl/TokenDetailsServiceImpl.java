package com.github.stanislavbukaevsky.userservice.token.impl;

import com.github.stanislavbukaevsky.userservice.security.impl.UserSecurity;
import com.github.stanislavbukaevsky.userservice.token.TokenDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.EXCEPTION_MESSAGE_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.EXPIRED_JWT_EXCEPTION_MESSAGE_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.MALFORMED_JWT_EXCEPTION_MESSAGE_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.SIGNATURE_EXCEPTION_MESSAGE_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.ExceptionTextMessageConstant.UNSUPPORTED_JWT_EXCEPTION_MESSAGE_SERVICE;

/**
 * Класс с бизнес-логикой для генерации и валидации access и refresh токенов
 */
@Component
@Slf4j
public class TokenDetailsServiceImpl implements TokenDetailsService {
    private final SecretKey secretAccessKey;
    private final SecretKey secretRefreshKey;
    private final String issuer;

    public TokenDetailsServiceImpl(@Value("${token.secret.access}") String secretAccessKey,
                                   @Value("${token.secret.refresh}") String secretRefreshKey,
                                   @Value("${token.user.issuer}") String issuer) {
        this.secretAccessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccessKey));
        this.secretRefreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRefreshKey));
        this.issuer = issuer;
    }

    /**
     * Этот метод генерирует access токен
     *
     * @param userSecurity пользовательские данные
     * @return Возвращает сгенерированный JWT access токен в строковом виде
     */
    @Override
    public String generateAccessToken(@NonNull UserSecurity userSecurity) {
        final String role = "role";
        final String email = "email";
        final Date issuedAt = getIssuedAt();
        final Date accessExpiration = getAccessExpiration();
        final Map<String, Object> claims = new HashMap<>() {{
            put(role, userSecurity.getUser().getRole());
            put(email, userSecurity.getUser().getEmail());
        }};

        return Jwts.builder()
                .issuer(issuer)
                .claims(claims)
                .subject(userSecurity.getUsername())
                .issuedAt(issuedAt)
                .expiration(accessExpiration)
                .signWith(secretAccessKey)
                .compact();
    }

    /**
     * Этот метод генерирует refresh токен
     *
     * @param userSecurity пользовательские данные
     * @return Возвращает сгенерированный JWT refresh токен в строковом виде
     */
    @Override
    public String generateRefreshToken(@NonNull UserSecurity userSecurity) {
        final Date issuedAt = getIssuedAt();
        final Date refreshExpiration = getRefreshExpiration();

        return Jwts.builder()
                .issuer(issuer)
                .subject(userSecurity.getUsername())
                .issuedAt(issuedAt)
                .expiration(refreshExpiration)
                .signWith(secretRefreshKey)
                .compact();
    }

    /**
     * Этот метод генерирует дату истечения срока действия access токена
     *
     * @return Возвращает дату истечения срока действия access токена
     */
    @Override
    public Date getAccessExpiration() {
        final LocalDateTime dateTime = LocalDateTime.now();
        final long validityTimeAccessToken = 60;
        final Instant accessExpirationInstant = dateTime
                .plusMinutes(validityTimeAccessToken)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(accessExpirationInstant);
    }

    /**
     * Этот метод генерирует дату истечения срока действия refresh токена
     *
     * @return Возвращает дату истечения срока действия refresh токена
     */
    @Override
    public Date getRefreshExpiration() {
        final LocalDateTime dateTime = LocalDateTime.now();
        final long validityTimeRefreshToken = 30;
        final Instant refreshExpirationInstant = dateTime
                .plusDays(validityTimeRefreshToken)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(refreshExpirationInstant);
    }

    /**
     * Этот метод отвечает за проверку валидности access токена
     *
     * @param accessToken access токен
     * @return Возвращает true или false, в зависимости от того валиден access токен или нет
     */
    @Override
    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, secretAccessKey);
    }

    /**
     * Этот метод отвечает за проверку валидности refresh токена
     *
     * @param refreshToken refresh токен
     * @return Возвращает true или false, в зависимости от того валиден refresh токен или нет
     */
    @Override
    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, secretRefreshKey);
    }

    /**
     * Этот метод вытягивает из access токена данные о пользователе
     *
     * @param accessToken access токен
     * @return Возвращает полученные данные о пользователе по его access токену
     */
    @Override
    public Claims getAccessClaims(@NonNull String accessToken) {
        return getClaims(accessToken, secretAccessKey);
    }

    /**
     * Этот метод вытягивает из refresh токена данные о пользователе
     *
     * @param refreshToken refresh токен
     * @return Возвращает полученные данные о пользователе по его refresh токену
     */
    @Override
    public Claims getRefreshClaims(@NonNull String refreshToken) {
        return getClaims(refreshToken, secretRefreshKey);
    }

    private Date getIssuedAt() {
        final LocalDateTime dateTime = LocalDateTime.now();
        return Timestamp.valueOf(dateTime);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secretKey) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error(EXPIRED_JWT_EXCEPTION_MESSAGE_SERVICE, expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error(UNSUPPORTED_JWT_EXCEPTION_MESSAGE_SERVICE, unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error(MALFORMED_JWT_EXCEPTION_MESSAGE_SERVICE, mjEx);
        } catch (SignatureException sEx) {
            log.error(SIGNATURE_EXCEPTION_MESSAGE_SERVICE, sEx);
        } catch (Exception e) {
            log.error(EXCEPTION_MESSAGE_SERVICE, e);
        }
        return false;
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secretKey) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
