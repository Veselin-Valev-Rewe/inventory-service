package com.example.inventory_service.dto.user;

import com.example.inventory_service.data.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;

    @NotBlank()
    @Size(max = 36)
    private String username;

    @NotBlank()
    @Size(max = 50)
    private String firstName;

    @NotBlank()
    @Size(max = 50)
    private String lastName;

    private UserRole role;

    private int warehouseId;
}
