package com.github.stanislavbukaevsky.userservice.controller;

import com.github.stanislavbukaevsky.userservice.dto.AccessTokenRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.AccessTokenResponseDto;
import com.github.stanislavbukaevsky.userservice.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс-контроллер для работы с методами получения нового access-токена
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
@Tag(name = "Работа с токенами пользователя", description = "Позволяет управлять методами по работе с токенами пользователя")
public class TokenController {
    private final TokenService tokenService;

    /**
     * Этот метод позволяет получить новый access-токен для зарегистрированного пользователя на платформе
     *
     * @param request класс-DTO с refresh-токеном, выданный пользователю приложением
     * @return Возвращает ответ с личной информацией о пользователе с новым, сгенерированным access-токеном
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новый access-токен успешно выдан (OK)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccessTokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Неккоректный запрос (Bad Request)"),
            @ApiResponse(responseCode = "401", description = "Неаутентифицированный пользователь (Unauthorized)"),
            @ApiResponse(responseCode = "403", description = "Пользователю запрещен вход на этот ресурс (Forbidden)"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден (Not Found)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера (Internal Server Error)")
    })
    @Operation(summary = "Метод для получения новго access-токена",
            description = "Позволяет сгенерировать новый access-токен для зарегистрированных пользователей на платформе")
    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/access-new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessTokenResponseDto> replaceAccessToken(@Valid @RequestBody AccessTokenRequestDto request) {
        AccessTokenResponseDto response = tokenService.replaceAccessToken(request);
        return ResponseEntity.ok(response);
    }
}
