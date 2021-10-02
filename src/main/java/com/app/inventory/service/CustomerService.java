package com.app.inventory.service;

import com.app.inventory.dto.CustomerDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.model.Customer;
import com.app.inventory.model.CustomerContact;
import com.app.inventory.model.key.CustomerContactKey;
import com.app.inventory.repository.CustomerContactRepository;
import com.app.inventory.repository.CustomerRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;

    public List<Customer> getCustomerList(){
        return customerRepository.findAll();
    }

    public ResponseEntity<?> createNewCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        List<CustomerContact> customerContactList = new ArrayList<>();

        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setAddress1(customerDto.getAddress1());
        customer.setAddress2(customerDto.getAddress2());
        customer.setAddress3(customerDto.getAddress3());
        customer.setEmail(customerDto.getEmail());
        customer.setCreatedDate(LocalDateTime.now());

        customerRepository.save(customer);

        customer = customerRepository.findTopByOrderByIdDesc();

        CustomerContact customerContact;
        CustomerContactKey customerContactKey;
        for (String contactNumber: customerDto.getContactList()) {
            customerContactKey = new CustomerContactKey();
            customerContactKey.setCustomerId(customer.getId());
            customerContactKey.setContact(contactNumber);

            customerContact = new CustomerContact();
            customerContact.setCustomerContactKey(customerContactKey);
            customerContactList.add(customerContact);
        }

        customerContactRepository.saveAll(customerContactList);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Success");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }
}
