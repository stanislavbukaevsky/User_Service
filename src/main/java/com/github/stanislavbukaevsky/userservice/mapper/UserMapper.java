package com.github.stanislavbukaevsky.userservice.mapper;

import com.github.stanislavbukaevsky.userservice.dto.RegistrationRequestDto;
import com.github.stanislavbukaevsky.userservice.dto.RegistrationResponseDto;
import com.github.stanislavbukaevsky.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер-интерфейс, который преобразует информацию о пользователе в DTO
 */
@Mapper
public interface UserMapper {
    RegistrationResponseDto mappingRegistrationResponseDto(User user);
    @Mapping(ignore = true, target = "password")
    User mappingEntityUser(RegistrationRequestDto request);
}
