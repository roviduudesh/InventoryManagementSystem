package com.app.inventory.controller;

import com.app.inventory.dto.CustomerDto;
import com.app.inventory.model.Customer;
import com.app.inventory.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> addNewCustomer(@RequestBody CustomerDto customerDto){
        ResponseEntity<?> responseEntity = customerService.createNewCustomer(customerDto);
        return responseEntity;
    }
}
