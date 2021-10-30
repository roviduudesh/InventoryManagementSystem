package com.app.inventory.repository;

import com.app.inventory.model.Item;
import com.app.inventory.model.OrderItem;
import com.app.inventory.model.key.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKey> {

    List<Item> findByOrderItemKey_ItemId(int itemId);
}
