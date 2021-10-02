package com.app.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "stock")
public class Stock implements Serializable {

    @Id
    @Column(name = "stock_id")
    private int id;

    @Column(name = "stock_date")
    private LocalDateTime stockDate;

    @Column(name = "stock_qty")
    private double quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sup_id", referencedColumnName ="sup_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Supplier supplier;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;
}
