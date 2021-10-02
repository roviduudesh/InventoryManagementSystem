package com.app.inventory.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "supplier")
public class Supplier implements Serializable {

    @Id
    @Column(name = "sup_id")
    private int id;

    @Column(name = "sup_name")
    private String name;

    @Column(name = "sup_add_1")
    private String line1;

    @Column(name = "sup_add_2")
    private String line2;

    @Column(name = "sup_add_3")
    private String line3;

    @Column(name = "sup_email")
    private String email;

    @Column(name = "sup_created_date")
    private LocalDateTime createdDate;

    @JoinColumn(name = "sup_id", referencedColumnName = "sup_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<SupplierContact> supplierContactList;
}
