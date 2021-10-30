package com.app.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`order`")
@Data
public class Order implements Serializable{

    @Id
    @Column(name = "order_id")
    private String id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cus_id", referencedColumnName = "cus_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<OrderItem> orderItemList;
}
