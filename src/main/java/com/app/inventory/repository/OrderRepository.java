package com.app.inventory.repository;

import com.app.inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findById(String id);
    List<Order> findAllByUser_Id(int userId);
}
