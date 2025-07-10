package com.example.inventory_service.controller;

import com.example.inventory_service.dto.user.CreateUserDto;
import com.example.inventory_service.dto.user.UpdateUserDto;
import com.example.inventory_service.dto.user.UserDto;
import com.example.inventory_service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getWarehouseUsers(@RequestParam @Positive int warehouseId, Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(warehouseId, pageable));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserDto userDto) {
        var user = userService.createUser(userDto);
        URI location = URI.create("/api/users/" + user.getId());
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable @Positive int id, @RequestBody @Valid UpdateUserDto userDto) {
        var user = userService.updateUser(id, userDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/warehouses/add/{warehouseId}")
    public ResponseEntity<UserDto> removeUserFromWarehouse(@PathVariable @Positive int id, @PathVariable @Positive int warehouseId) {
        var user = userService.addUserToWarehouse(id, warehouseId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/warehouses/remove")
    public ResponseEntity<UserDto> removeUserFromWarehouse(@PathVariable int id) {
        var user = userService.removeUserFromWarehouse(id);
        return ResponseEntity.ok(user);
    }
}
