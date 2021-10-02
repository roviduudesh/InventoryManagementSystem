package com.app.inventory.dto;

import com.app.inventory.model.Supplier;
import com.app.inventory.model.SupplierContact;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class NewSupplierDto {
    private String name;
    private String address1;
    private String address2;
    private String address3;
    private String email;
    private List<String> contactList;
}
