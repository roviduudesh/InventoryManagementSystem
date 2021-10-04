package com.app.inventory.repository;

import com.app.inventory.model.OrderItem;
import com.app.inventory.model.key.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKey> {
}
