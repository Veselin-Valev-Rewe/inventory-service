package com.example.inventory_service.data.repository;

import com.example.inventory_service.data.entity.ProducedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducedMessageRepository extends JpaRepository<ProducedMessage, Integer> {
}
