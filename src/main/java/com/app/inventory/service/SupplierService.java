package com.app.inventory.service;

import com.app.inventory.dto.NewSupplierDto;
import com.app.inventory.dto.ResponseDto;
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

@Service
@Data
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierContactRepository supplierContactRepository;

    public List<Supplier> getSupplierList(){
        List<Supplier> supplierList = supplierRepository.findAll();
        return supplierList;
    }

    public ResponseEntity<?> createNewSupplier(NewSupplierDto newSupplierDto) {
        Supplier supplier = new Supplier();
        List<SupplierContact> supplierContactList = new ArrayList<>();

        supplier.setName(newSupplierDto.getName());
        supplier.setLine1(newSupplierDto.getAddress1());
        supplier.setLine2(newSupplierDto.getAddress2());
        supplier.setLine3(newSupplierDto.getAddress3());
        supplier.setCreatedDate(LocalDateTime.now());
        supplier.setEmail(newSupplierDto.getEmail());
        supplierRepository.save(supplier);

        supplier = supplierRepository.findTopByOrderByIdDesc();

        SupplierContact supplierContact;
        SupplierContactKey supplierContactKey;
        for (String contactNumber: newSupplierDto.getContactList()) {
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
        responseDto.setMessage("Success");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
