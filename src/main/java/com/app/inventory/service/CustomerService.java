package com.app.inventory.service;

import com.app.inventory.dto.CustomerDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.SupplierDto;
import com.app.inventory.model.Customer;
import com.app.inventory.model.CustomerContact;
import com.app.inventory.model.Supplier;
import com.app.inventory.model.SupplierContact;
import com.app.inventory.model.key.CustomerContactKey;
import com.app.inventory.model.key.SupplierContactKey;
import com.app.inventory.repository.CustomerContactRepository;
import com.app.inventory.repository.CustomerRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Data
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;

    public ResponseEntity<?> getCustomerList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Customer> customerList = customerRepository.findAll();
            List<CustomerDto> customerDtoList = new ArrayList<>();
            CustomerDto customerDto;
            String contact;
            for (Customer customer : customerList) {
                customerDto = new CustomerDto();
                customerDto.setId(customer.getId());
                customerDto.setFirstName(customer.getFirstName());
                customerDto.setLastName(customer.getLastName());
                customerDto.setAddress1(customer.getAddress1());
                customerDto.setAddress2(customer.getAddress2());
                customerDto.setAddress3(customer.getAddress3());
                customerDto.setEmail(customer.getEmail());
                contact = "";
                if(customer.getCustomerContactList().size() > 0) {
                    for (CustomerContact customerContact : customer.getCustomerContactList()) {
                        contact = contact + customerContact.getCustomerContactKey().getContact() + ", ";
                    }
                    customerDto.setContact(contact.substring(0, contact.length() - 2));
                }
                customerDtoList.add(customerDto);
            }
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Customer List");
            responseDto.setData(customerDtoList);
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
            List<String> contactList = Arrays.asList(customerDto.getContact().split("\\s*,\\s*"));
            for (String contactNumber : contactList) {
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

                List<CustomerContact> customerContactList = new ArrayList<>();
                CustomerContact customerContact;
                CustomerContactKey customerContactKey;
                List<String> contactList = Arrays.asList(customerDto.getContact().split("\\s*,\\s*"));
                for (String contactNumber : contactList) {
                    customerContactKey = new CustomerContactKey();
                    customerContactKey.setCustomerId(customer.getId());
                    customerContactKey.setContact(contactNumber);

                    customerContact = new CustomerContact();
                    customerContact.setCustomerContactKey(customerContactKey);
                    customerContactList.add(customerContact);
                }
                customerContactRepository.deleteByCustomerContactKey_CustomerId(customer.getId());
                customerContactRepository.saveAll(customerContactList);

                status = HttpStatus.OK.value();
                message = "Successfully Updated";
            } else{
                status = HttpStatus.NO_CONTENT.value();
                message = "Customer Not Found";
            }
        } catch (Exception ex){
            ex.printStackTrace();
            status = HttpStatus.EXPECTATION_FAILED.value();
            message = "Technical Failure";
        }
        responseDto.setStatus(status);
        responseDto.setMessage(message);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteCustomer(int customerId) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Customer> customerOptional = customerRepository.findById(customerId);
            if (customerOptional.isPresent()) {
                if(customerOptional.get().getOrderList().size() > 0) {
                    status = HttpStatus.NO_CONTENT.value();
                    message = "Unable to Delete, Customer has Orders !!!";
                } else {
                    customerRepository.deleteCustomer(customerId);
                    status = HttpStatus.OK.value();
                    message = "Successfully Deleted";
                }
            } else{
                status = HttpStatus.NO_CONTENT.value();
                message = "Customer Not Found";
            }
        } catch (Exception ex){
            ex.printStackTrace();
            status = HttpStatus.EXPECTATION_FAILED.value();
            message = "Technical Failure";
        }
        responseDto.setStatus(status);
        responseDto.setMessage(message);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
