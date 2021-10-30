package com.app.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerDto {
    private int id;
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String address3;
    private String email;
    private String contact;
}
