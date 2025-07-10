package com.example.inventory_service.service;

import com.example.inventory_service.dto.user.CreateUserDto;
import com.example.inventory_service.dto.user.UpdateUserDto;
import com.example.inventory_service.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> getUsers(int warehouseId, Pageable pageable);

    UserDto createUser(CreateUserDto userDto);

    UserDto updateUser(int id, UpdateUserDto userDto);

    UserDto addUserToWarehouse(int id, int warehouseId);

    UserDto removeUserFromWarehouse(int id);

    void deleteUser(int id);
}
