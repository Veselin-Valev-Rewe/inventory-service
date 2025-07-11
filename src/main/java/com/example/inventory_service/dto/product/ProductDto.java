package com.example.inventory_service.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private int id;

    private String title;

    private String description;

    private String imageUrl;

    private String uniqueIdentifierNan;

    private String uniqueIdentifierGtin;

    private LocalDateTime created;

    private LocalDateTime updated;
}
