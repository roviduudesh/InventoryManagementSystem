package com.app.inventory.service;

import com.app.inventory.dto.*;
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
import static com.app.inventory.service.Common.*;

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
        try {
            List<Customer> customerList = customerRepository.findAllByOrderByFirstNameAsc();
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
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, customerDtoList), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> createNewCustomer(CustomerDto customerDto) {
        try {
            ValidateDto validateDto = validateCustomer(customerDto);
            if(!validateDto.isValid()){
                return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), validateDto.getMessage(), null), HttpStatus.OK);
            } else {
                Customer customer = new Customer();

                customer.setFirstName(customerDto.getFirstName());
                customer.setLastName(customerDto.getLastName());
                customer.setAddress1(customerDto.getAddress1());
                customer.setAddress2(customerDto.getAddress2());
                customer.setAddress3(customerDto.getAddress3());
                customer.setEmail(customerDto.getEmail());
                customer.setCreatedDate(LocalDateTime.now());
                customerRepository.save(customer);

                customer = customerRepository.findTopByOrderByIdDesc();
                if(customerDto.getContact() != null) {
                    setCustomerContact(customer.getId(), customerDto.getContact(), "insert");
                }
                return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
            }
        } catch(Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    ValidateDto validateCustomer(CustomerDto customerDto){
        ValidateDto validateDto = new ValidateDto();
        Optional<Customer> customerOptional;
        Optional<String> invalidContact = Optional.empty();
        boolean isValid = true;
        String message = null;
        List<Customer> customerList = customerRepository.findAll();
        customerOptional = customerList.stream()
                .filter(c -> c.getId() != customerDto.getId() && !c.getEmail().isEmpty() && c.getEmail().equalsIgnoreCase(customerDto.getEmail().trim()))
                .findFirst();
        if(customerDto.getContact() != null) {
            List<String> contactList = Arrays.asList(customerDto.getContact().split("\\s*,\\s*"));
            invalidContact = contactList.stream()
                    .filter(c -> !c.isEmpty() && c.length() != 10)
                    .findAny();
        }
        if(invalidContact.isPresent()){
            isValid = false;
            message = "Invalid Contact Number";
        } else if (customerOptional.isPresent()) {
            isValid = false;
            message = "Email Exists";
        }
        validateDto.setValid(isValid);
        validateDto.setMessage(message);
        return validateDto;
    }

    void setCustomerContact(int customerId, String contact, String process){
        List<CustomerContact> customerContactList = new ArrayList<>();
        CustomerContact customerContact;
        CustomerContactKey customerContactKey;
        List<String> contactList = Arrays.asList(contact.split("\\s*,\\s*"));

        for (String contactNumber : contactList) {
            customerContactKey = new CustomerContactKey();
            customerContactKey.setCustomerId(customerId);
            customerContactKey.setContact(contactNumber);

            customerContact = new CustomerContact();
            customerContact.setCustomerContactKey(customerContactKey);
            customerContactList.add(customerContact);
        }
        if(process.equalsIgnoreCase("update")){
            customerContactRepository.deleteByCustomerContactKey_CustomerId(customerId);
        }
        customerContactRepository.saveAll(customerContactList);
    }

    public ResponseEntity<?> updateCustomer(int customerId, CustomerDto customerDto) {
        try {
            ValidateDto validateDto = validateCustomer(customerDto);

            if(!validateDto.isValid()){
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), validateDto.getMessage(), null), HttpStatus.NOT_ACCEPTABLE);
            } else {
                Optional<Customer> customerOptional = customerRepository.findById(customerId);
                if (customerOptional.isPresent()) {
                    Customer customer = customerOptional.get();
                    customer.setFirstName(customerDto.getFirstName());
                    customer.setLastName(customerDto.getLastName());
                    customer.setAddress1(customerDto.getAddress1());
                    customer.setAddress2(customerDto.getAddress2());
                    customer.setAddress3(customerDto.getAddress3());
                    customer.setEmail(customerDto.getEmail());
                    customerRepository.save(customer);

                    if(customerDto.getContact() != null) {
                        setCustomerContact(customerId, customerDto.getContact(), "update");
                    }
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Customer Not Found", null), HttpStatus.NO_CONTENT);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), SUCCESS, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> deleteCustomer(int customerId) {
        try {
            Optional<Customer> customerOptional = customerRepository.findById(customerId);
            if (customerOptional.isPresent()) {
                if(customerOptional.get().getOrderList().size() > 0) {
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Unable to Delete, Customer has Orders !!!", null), HttpStatus.NO_CONTENT);
                } else {
                    customerRepository.deleteCustomer(customerId);
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
                }
            } else{
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Customer Not Found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> getCusIdNameList() {
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Customer> customerList = customerRepository.findAllByOrderByFirstNameAsc();
            List<IdNameDto> idNameDtoList = new ArrayList<>();
            IdNameDto idNameDto;
            for (Customer customer : customerList) {
                idNameDto = new IdNameDto();
                idNameDto.setId(customer.getId());
                idNameDto.setName(customer.getFirstName() + " " + customer.getLastName());

                idNameDtoList.add(idNameDto);
            }
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Customer List");
            responseDto.setData(idNameDtoList);
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), EXCEPTION, idNameDtoList), HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
