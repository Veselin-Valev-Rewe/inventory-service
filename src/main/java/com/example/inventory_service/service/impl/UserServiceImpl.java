package com.example.inventory_service.service.impl;

import com.example.inventory_service.dto.user.CreateUserDto;
import com.example.inventory_service.dto.user.UpdateUserDto;
import com.example.inventory_service.dto.user.UserDto;
import com.example.inventory_service.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserDto> getUsers(Pageable pageable) {
        return List.of();
    }

    @Override
    public UserDto getUserById(int id) {
        return null;
    }

    @Override
    public UserDto createUser(CreateUserDto userDto) {
        return null;
    }

    @Override
    public UserDto updateUser(UpdateUserDto userDto) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }
}
