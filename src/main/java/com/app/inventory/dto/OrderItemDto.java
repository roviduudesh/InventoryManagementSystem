package com.app.inventory.dto;

import lombok.Data;

@Data
public class OrderItemDto {

    private int itemId;
    private double quantity;
    private double amount;
}
