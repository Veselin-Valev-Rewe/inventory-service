package com.example.inventory_service.data.repository;

import com.example.inventory_service.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
