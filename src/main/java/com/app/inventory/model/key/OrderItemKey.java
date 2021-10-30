package com.app.inventory.model.key;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class OrderItemKey implements Serializable {

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "item_id")
    private int itemId;
}
