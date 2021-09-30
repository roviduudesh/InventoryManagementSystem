package com.app.inventory.controller;

import com.app.inventory.model.Customer;
import com.app.inventory.model.SupplierContact;
import com.app.inventory.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/all")
    public List<Customer> getCustomerList(){
        return customerService.getCustomerList();
    }
}
