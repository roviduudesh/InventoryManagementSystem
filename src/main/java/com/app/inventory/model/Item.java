package com.app.inventory.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "item")
public class Item implements Serializable {

    @Id
    @Column(name = "item_id")
    private int id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_quantity")
    private double quantity;

    @Column(name = "item_price")
    private double price;

    @Column(name = "item_warranty")
    private int warranty;

    @Column(name = "item_created_date")
    private LocalDateTime createdDate;
}
