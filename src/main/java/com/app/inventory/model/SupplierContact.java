package com.app.inventory.model;

import javax.persistence.*;

@Entity
@Table(name = "sup_contact")
public class SupplierContact {

    @Id
    @Column(name = "sup_contact_id")
    private int supplierContactId;

    @Column(name = "sup_id")
    private int supplierId;

    @Column(name = "contact")
    private String contact;

    public int getSupplierContactId() {
        return supplierContactId;
    }

    public void setSupplierContactId(int supplierContactId) {
        this.supplierContactId = supplierContactId;
    }

    public int getSupplier() {
        return supplierId;
    }

    public void setSupplier(int supplier) {
        this.supplierId = supplier;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
