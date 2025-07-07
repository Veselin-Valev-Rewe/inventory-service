package com.example.inventory_service.data.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {
    private String title;

    private String description;

    private String imageUrl;

    private String uniqueIdentifierNan;

    private String uniqueIdentifierGtin;
}
