package com.app.inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cus_contact")
public class CustomerContact {

    @Id
    @Column(name = "cus_contact_id")
    private int id;

    @Column(name = "cus_id")
    private String customerId;

    @Column(name = "contact")
    private String contact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
