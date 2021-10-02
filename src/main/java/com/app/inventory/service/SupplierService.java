package com.app.inventory.service;

import com.app.inventory.dto.SupplierDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.model.Customer;
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

    public List<Supplier> getSupplierList(){
        List<Supplier> supplierList = supplierRepository.findAll();
        return supplierList;
    }

    public ResponseEntity<?> createNewSupplier(SupplierDto supplierDto) {
        Supplier supplier = new Supplier();
        List<SupplierContact> supplierContactList = new ArrayList<>();

        supplier.setName(supplierDto.getName());
        supplier.setLine1(supplierDto.getAddress1());
        supplier.setLine2(supplierDto.getAddress2());
        supplier.setLine3(supplierDto.getAddress3());
        supplier.setCreatedDate(LocalDateTime.now());
        supplier.setEmail(supplierDto.getEmail());
        supplierRepository.save(supplier);

        supplier = supplierRepository.findTopByOrderByIdDesc();

        SupplierContact supplierContact;
        SupplierContactKey supplierContactKey;
        for (String contactNumber: supplierDto.getContactList()) {
            supplierContactKey = new SupplierContactKey();
            supplierContactKey.setSupplierId(supplier.getId());
            supplierContactKey.setContact(contactNumber);

            supplierContact = new SupplierContact();
            supplierContact.setSupConKey(supplierContactKey);
            supplierContactList.add(supplierContact);
        }

        supplierContactRepository.saveAll(supplierContactList);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Successfully Inserted");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateSupplier(int customerId, SupplierDto supplierDto) {

        Optional<Supplier> supplierOptional = supplierRepository.findById(customerId);

        Supplier supplier = supplierOptional.get();

        supplier.setName(supplierDto.getName());
        supplier.setLine1(supplierDto.getAddress1());
        supplier.setLine2(supplierDto.getAddress2());
        supplier.setLine3(supplierDto.getAddress3());
        supplier.setEmail(supplierDto.getEmail());
        supplierRepository.save(supplier);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Successfully Updated");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
