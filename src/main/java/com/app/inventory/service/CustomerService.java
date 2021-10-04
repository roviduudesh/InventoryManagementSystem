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
import java.util.Optional;

@Service
@Data
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;

    public ResponseEntity<?> getCustomerList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Customer> customerList = customerRepository.findAll();
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Customer List");
            responseDto.setData(customerList);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createNewCustomer(CustomerDto customerDto) {
        ResponseDto responseDto = new ResponseDto();
        try {
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
            for (String contactNumber : customerDto.getContactList()) {
                customerContactKey = new CustomerContactKey();
                customerContactKey.setCustomerId(customer.getId());
                customerContactKey.setContact(contactNumber);

                customerContact = new CustomerContact();
                customerContact.setCustomerContactKey(customerContactKey);
                customerContactList.add(customerContact);
            }
            customerContactRepository.saveAll(customerContactList);

            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Success");
        } catch(Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateCustomer(int customerId, CustomerDto customerDto) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Customer> customerOptional = customerRepository.findById(customerId);
            if(customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                customer.setFirstName(customerDto.getFirstName());
                customer.setLastName(customerDto.getLastName());
                customer.setAddress1(customerDto.getAddress1());
                customer.setAddress2(customerDto.getAddress2());
                customer.setAddress3(customerDto.getAddress3());
                customer.setEmail(customerDto.getEmail());
                customerRepository.save(customer);
                status = HttpStatus.OK.value();
                message = "Success";
            } else{
                status = HttpStatus.NO_CONTENT.value();
                message = "Customer Not Found";
            }
            responseDto.setStatus(status);
            responseDto.setMessage(message);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
