package com.app.inventory.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class NewOrderDto {
    private int customerId;
    private int itemId;
    private double quantity;
    private double amount;
    private int userId;
}
