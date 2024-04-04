package com.github.stanislavbukaevsky.userservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.PASSWORD_ENCODER_ENCODE_MESSAGE_LOGGER_SERVICE;
import static com.github.stanislavbukaevsky.userservice.constant.LoggerTextMessageConstant.PASSWORD_ENCODER_MATCHES_MESSAGE_LOGGER_SERVICE;

/**
 * Этот класс для шифрования пароля пользователя.
 * Реализует интерфейс {@link PasswordEncoder}
 */
@Component
@Slf4j
public class PBKDF2Encoder implements PasswordEncoder {
    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA512";
    @Value("${security.password.encoder.secret}")
    private String secret;
    @Value("${security.password.encoder.iteration}")
    private Integer iteration;
    @Value("${security.password.encoder.keyLength}")
    private Integer keyLength;

    /**
     * Этот метод зашифровывает полученный от пользователя пароль
     *
     * @param rawPassword пароль пользователя
     * @return Возвращает зашифрованный пароль пользователя
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            byte[] result = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                            secret.getBytes(),
                            iteration,
                            keyLength)).getEncoded();

            log.info(PASSWORD_ENCODER_ENCODE_MESSAGE_LOGGER_SERVICE, rawPassword);
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Этот метод проверяет полученный пароль от пользователя и сравнивает его с зашифрованным паролем в базе данных
     *
     * @param rawPassword     полученный пароль от пользователя
     * @param encodedPassword зашифрованный пароль, сохраненный в базе данных
     * @return Возвращает true, если пароли совпадают
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        log.info(PASSWORD_ENCODER_MATCHES_MESSAGE_LOGGER_SERVICE, rawPassword, encodedPassword);
        return encode(rawPassword).equals(encodedPassword);
    }
}
