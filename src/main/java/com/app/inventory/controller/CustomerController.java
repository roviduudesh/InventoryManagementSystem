package com.app.inventory.controller;

import com.app.inventory.dto.CustomerDto;
import com.app.inventory.model.Customer;
import com.app.inventory.service.CustomerService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping(path = "/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getCustomerList(){
        return customerService.getCustomerList();
    }

    @PostMapping
    public ResponseEntity<?> addNewCustomer(@RequestBody CustomerDto customerDto){
        ResponseEntity<?> responseEntity = customerService.createNewCustomer(customerDto);
        return responseEntity;
    }

    @PutMapping(path = "{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable("customerId") int customerId,
                                            @RequestBody CustomerDto customerDto){
        ResponseEntity<?> responseEntity = customerService.updateCustomer(customerId, customerDto);
        return responseEntity;
    }
}
