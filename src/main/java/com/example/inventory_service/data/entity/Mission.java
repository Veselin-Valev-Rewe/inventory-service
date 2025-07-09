package com.example.inventory_service.data.entity;

import com.example.inventory_service.data.enums.MissionStatus;
import com.example.inventory_service.data.enums.OperationType;
import jakarta.persistence.*;
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
public class Mission extends BaseEntity {
    private int finalCount;

    private int originalCount;

    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
}
