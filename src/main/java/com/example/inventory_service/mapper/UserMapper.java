package com.example.inventory_service.mapper;

import com.example.inventory_service.data.entity.User;
import com.example.inventory_service.dto.user.CreateUserDto;
import com.example.inventory_service.dto.user.UpdateUserDto;
import com.example.inventory_service.dto.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(CreateUserDto userDto);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UpdateUserDto dto, @MappingTarget User user);
}