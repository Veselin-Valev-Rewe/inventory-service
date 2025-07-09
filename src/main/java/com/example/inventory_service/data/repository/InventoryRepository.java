package com.example.inventory_service.data.repository;

import com.example.inventory_service.data.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByProductIdAndWarehouseId(int productId, int warehouseId);
}
