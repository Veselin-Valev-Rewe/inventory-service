package com.example.inventory_service.service;

import com.example.inventory_service.dto.user.CreateUserDto;
import com.example.inventory_service.dto.user.UpdateUserDto;
import com.example.inventory_service.dto.user.UserDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(Pageable pageable);

    UserDto getUserById(int id);

    UserDto createUser(@Valid CreateUserDto userDto);

    UserDto updateUser(@Valid UpdateUserDto userDto);

    void deleteUser(int id);
}
