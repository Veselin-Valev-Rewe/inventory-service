package com.example.inventory_service.data.repository;

import com.example.inventory_service.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findByWarehouseId(Integer warehouseId, Pageable pageable);
}
