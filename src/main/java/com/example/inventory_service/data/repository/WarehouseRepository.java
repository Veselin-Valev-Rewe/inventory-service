package com.example.inventory_service.data.repository;

import com.example.inventory_service.data.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}
