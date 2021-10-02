package com.app.inventory.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_first_name")
    private String firstName;

    @Column(name = "user_last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_contact")
    private String contact;

}
