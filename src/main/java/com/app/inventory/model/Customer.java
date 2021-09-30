package com.app.inventory.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

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
    private String createdDate;

    @JoinColumn(name = "cus_id", referencedColumnName = "cus_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<CustomerContact> customerContactList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
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

    public List<CustomerContact> getCustomerContactList() {
        return customerContactList;
    }

    public void setCustomerContactList(List<CustomerContact> customerContactList) {
        this.customerContactList = customerContactList;
    }
}
