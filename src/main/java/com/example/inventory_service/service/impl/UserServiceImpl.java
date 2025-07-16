package com.example.inventory_service.service.impl;

import com.example.inventory_service.data.entity.User;
import com.example.inventory_service.data.entity.Warehouse;
import com.example.inventory_service.data.repository.UserRepository;
import com.example.inventory_service.data.repository.WarehouseRepository;
import com.example.inventory_service.dto.user.CreateUserDto;
import com.example.inventory_service.dto.user.UpdateUserDto;
import com.example.inventory_service.dto.user.UserDto;
import com.example.inventory_service.mapper.UserMapper;
import com.example.inventory_service.service.UserService;
import com.example.inventory_service.util.message.ErrorMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserDto> getUsers(int warehouseId, Pageable pageable) {
        var usersPage = userRepository.findByWarehouseId(warehouseId, pageable);
        return usersPage.map(userMapper::toUserDto);
    }

    @Override
    public UserDto createUser(CreateUserDto userDto) {
        var user = userMapper.toUser(userDto);
        var warehouse = getWarehouse(userDto.getWarehouseId());
        user.setWarehouse(warehouse);

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(int id, UpdateUserDto userDto) {
        var user = getUser(id);
        userMapper.updateUserFromDto(userDto, user);

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, id));
        }

        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserDetails(int id) {
        return userMapper.toUserDto(getUser(id));
    }

    @Override
    public UserDto addUserToWarehouse(int id, int warehouseId) {
        var user = getUser(id);
        var warehouse = getWarehouse(id);
        user.setWarehouse(warehouse);

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto removeUserFromWarehouse(int id) {
        var user = getUser(id);
        user.setWarehouse(null);

        return userMapper.toUserDto(userRepository.save(user));
    }

    private User getUser(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, id)));
    }

    private Warehouse getWarehouse(int id) {
        return warehouseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.WAREHOUSE_NOT_FOUND, id)));
    }
}