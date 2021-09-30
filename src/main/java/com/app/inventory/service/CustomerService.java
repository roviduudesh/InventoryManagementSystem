package com.app.inventory.service;

import com.app.inventory.model.Customer;
import com.app.inventory.model.SupplierContact;
import com.app.inventory.repository.CustomerRepository;
import com.app.inventory.repository.SupplierContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomerList(){
        return customerRepository.findAll();
    }
}
