package com.app.inventory.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "supplier")
public class Supplier {

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
    private String createdDate;

    @JoinColumn(name = "sup_id", referencedColumnName = "sup_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<SupplierContact> supplierContactList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<SupplierContact> getSupplierContactList() {
        return supplierContactList;
    }

    public void setSupplierContactList(List<SupplierContact> supplierContactList) {
        this.supplierContactList = supplierContactList;
    }
}
