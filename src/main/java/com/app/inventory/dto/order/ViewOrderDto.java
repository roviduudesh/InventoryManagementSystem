package com.app.inventory.dto.order;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ViewOrderDto {
    private String orderId;
    private LocalDateTime orderDate;
    private String customerName;
    private Double totalAmount;
    private List<OrderItemDto> orderItemList;
}
