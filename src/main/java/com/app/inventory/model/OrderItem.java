package com.app.inventory.model;

import com.app.inventory.model.key.OrderItemKey;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {

    @Id
    private OrderItemKey orderItemKey;

    @Column(name = "qty")
    private double quantity;

    @Column(name = "amount")
    private double amount;

}
