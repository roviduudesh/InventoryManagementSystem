package com.app.inventory.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    @Id
    @Column(name = "cus_id")
    private int id;

    @Column(name = "cus_first_name")
    private String firstName;

    @Column(name = "cus_last_name")
    private String lastName;

    @Column(name = "cus_add_1")
    private String address1;

    @Column(name = "cus_add_2")
    private String address2;

    @Column(name = "cus_add_3")
    private String address3;

    @Column(name = "cus_email")
    private String email;

    @Column(name = "cus_created_date")
    private LocalDateTime createdDate;

    @JoinColumn(name = "cus_id", referencedColumnName = "cus_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<CustomerContact> customerContactList;

    @JoinColumn(name = "cus_id", referencedColumnName = "cus_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orderList;
}
