package com.app.inventory.dto;

import com.app.inventory.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private int customerId;
    private int userId;
    private List<OrderItemDto> orderItemList;
}
