package com.app.inventory.dto;

import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String contact;
    private String level;
}
