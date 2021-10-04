package com.app.inventory.service;

import com.app.inventory.dto.SupplierDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.model.Customer;
import com.app.inventory.model.Stock;
import com.app.inventory.model.key.SupplierContactKey;
import com.app.inventory.model.Supplier;
import com.app.inventory.model.SupplierContact;
import com.app.inventory.repository.SupplierContactRepository;
import com.app.inventory.repository.SupplierRepository;
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
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierContactRepository supplierContactRepository;

    public ResponseEntity<?> getSupplierList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Supplier> supplierList = supplierRepository.findAll();
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Supplier List");
            responseDto.setData(supplierList);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createNewSupplier(SupplierDto supplierDto) {
        ResponseDto responseDto = new ResponseDto();
        try {
            Supplier supplier = new Supplier();
            supplier.setName(supplierDto.getName());
            supplier.setAddress1(supplierDto.getAddress1());
            supplier.setAddress2(supplierDto.getAddress2());
            supplier.setAddress3(supplierDto.getAddress3());
            supplier.setCreatedDate(LocalDateTime.now());
            supplier.setEmail(supplierDto.getEmail());
            supplierRepository.save(supplier);

            supplier = supplierRepository.findTopByOrderByIdDesc();
            List<SupplierContact> supplierContactList = new ArrayList<>();
            SupplierContact supplierContact;
            SupplierContactKey supplierContactKey;
            for (String contactNumber : supplierDto.getContactList()) {
                supplierContactKey = new SupplierContactKey();
                supplierContactKey.setSupplierId(supplier.getId());
                supplierContactKey.setContact(contactNumber);

                supplierContact = new SupplierContact();
                supplierContact.setSupConKey(supplierContactKey);
                supplierContactList.add(supplierContact);
            }
            supplierContactRepository.saveAll(supplierContactList);
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Successfully Inserted");
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateSupplier(int customerId, SupplierDto supplierDto) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Supplier> supplierOptional = supplierRepository.findById(customerId);
            if(supplierOptional.isPresent()){
                Supplier supplier = supplierOptional.get();
                supplier.setName(supplierDto.getName());
                supplier.setAddress1(supplierDto.getAddress1());
                supplier.setAddress2(supplierDto.getAddress2());
                supplier.setAddress3(supplierDto.getAddress3());
                supplier.setEmail(supplierDto.getEmail());
                supplierRepository.save(supplier);
                status = HttpStatus.OK.value();
                message = "Successfully Updated";
            } else {
                status = HttpStatus.NO_CONTENT.value();
                message = "Supplier Not Found";
            }
            responseDto.setStatus(status);
            responseDto.setMessage(message);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
