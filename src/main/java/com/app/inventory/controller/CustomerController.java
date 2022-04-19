package com.app.inventory.controller;

import com.app.inventory.dto.CustomerDto;
import com.app.inventory.model.Customer;
import com.app.inventory.service.CustomerService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getCustomerList(){
        return customerService.getCustomerList();
    }

    @GetMapping(value = "/customer_id_name")
    public ResponseEntity<?> getSupForStock(){
        ResponseEntity<?> responseDto =  customerService.getCusIdNameList();
        return responseDto;
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

    @DeleteMapping(path = "{customerId}")
    public ResponseEntity<?> deleteSupplier(@PathVariable("customerId") int customerId){
        ResponseEntity<?> responseEntity = customerService.deleteCustomer(customerId);
        return responseEntity;
    }
}
