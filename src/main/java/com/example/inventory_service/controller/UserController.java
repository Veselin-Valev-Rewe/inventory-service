package com.example.inventory_service.controller;

import com.example.inventory_service.dto.user.CreateUserDto;
import com.example.inventory_service.dto.user.UpdateUserDto;
import com.example.inventory_service.dto.user.UserDto;
import com.example.inventory_service.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Page<UserDto>> getWarehouseUsers(@RequestParam int warehouseId, Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(warehouseId, pageable));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserDto UserDto) {
        var user = userService.createUser(UserDto);
        URI location = URI.create("/api/users/" + user.getId());
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UpdateUserDto UserDto) {
        var user = userService.updateUser(UserDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/warehouses/add/{warehouseId}")
    public ResponseEntity<UserDto> removeUserFromWarehouse(@PathVariable int id, @PathVariable int warehouseId) {
        var user = userService.addUserToWarehouse(id, warehouseId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/warehouses/remove")
    public ResponseEntity<UserDto> removeUserFromWarehouse(@PathVariable int id) {
        var user = userService.removeUserFromWarehouse(id);
        return ResponseEntity.ok(user);
    }
}
