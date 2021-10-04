package com.app.inventory.repository;

import com.app.inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findTopByOrderByIdDesc();
}
