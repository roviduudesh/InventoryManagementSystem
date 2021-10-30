package com.app.inventory.dto.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ViewOrderDto {
    private String orderId;
    private LocalDateTime orderDate;
    private String customerName;
    private String itemString;
    private String qtyString;
    private Double totalAmount;
}
