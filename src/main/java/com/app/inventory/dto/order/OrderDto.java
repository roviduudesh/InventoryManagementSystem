package com.app.inventory.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private int customerId;
    private int userId;
    private List<OrderItemDto> orderItemList;
}
