package com.app.inventory.service;

import com.app.inventory.dto.IdNameDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Data
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierContactRepository supplierContactRepository;

    public ResponseEntity<?> getSupplierList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Supplier> supplierList = supplierRepository.findAllByOrderByNameAsc();
            List<SupplierDto> supplierDtoList = new ArrayList<>();
            SupplierDto supplierDto;
            String contact;
            for (Supplier supplier : supplierList) {
                supplierDto = new SupplierDto();
                supplierDto.setId(supplier.getId());
                supplierDto.setSupName(supplier.getName());
                supplierDto.setAddress1(supplier.getAddress1());
                supplierDto.setAddress2(supplier.getAddress2());
                supplierDto.setAddress3(supplier.getAddress3());
                supplierDto.setEmail(supplier.getEmail());
                contact = "";
                if(supplier.getSupplierContactList().size() > 0) {
                    for (SupplierContact supplierContact : supplier.getSupplierContactList()) {
                        contact = contact + supplierContact.getSupConKey().getContact() + ", ";
                    }
                    supplierDto.setContact(contact.substring(0, contact.length() - 2));
                }
                supplierDtoList.add(supplierDto);
            }
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Supplier List");
            responseDto.setData(supplierDtoList);
        } catch (Exception ex){
            ex.printStackTrace();
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
            supplier.setName(supplierDto.getSupName());
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
            List<String> contactList = Arrays.asList(supplierDto.getContact().split("\\s*,\\s*"));
            for (String contactNumber : contactList) {
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

    public ResponseEntity<?> updateSupplier(int supplierId, SupplierDto supplierDto) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Supplier> supplierOptional = supplierRepository.findById(supplierId);
            if(supplierOptional.isPresent()){
                Supplier supplier = supplierOptional.get();
                supplier.setName(supplierDto.getSupName());
                supplier.setAddress1(supplierDto.getAddress1());
                supplier.setAddress2(supplierDto.getAddress2());
                supplier.setAddress3(supplierDto.getAddress3());
                supplier.setEmail(supplierDto.getEmail());
                supplierRepository.save(supplier);

                List<SupplierContact> supplierContactList = new ArrayList<>();
                SupplierContact supplierContact;
                SupplierContactKey supplierContactKey;
                List<String> contactList = Arrays.asList(supplierDto.getContact().split("\\s*,\\s*"));
                for (String contactNumber : contactList) {
                    supplierContactKey = new SupplierContactKey();
                    supplierContactKey.setSupplierId(supplier.getId());
                    supplierContactKey.setContact(contactNumber);

                    supplierContact = new SupplierContact();
                    supplierContact.setSupConKey(supplierContactKey);
                    supplierContactList.add(supplierContact);
                }
                supplierContactRepository.deleteBySupConKey_SupplierId(supplier.getId());
                supplierContactRepository.saveAll(supplierContactList);

                status = HttpStatus.OK.value();
                message = "Successfully Updated";
            } else {
                status = HttpStatus.NO_CONTENT.value();
                message = "Supplier Not Found";
            }
            responseDto.setStatus(status);
            responseDto.setMessage(message);
        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteSupplier(int supplierId) {
        ResponseDto responseDto = new ResponseDto();

        Optional<Supplier> supplierOptional = supplierRepository.findById(supplierId);
        if(supplierOptional.isPresent()) {
            supplierRepository.deleteSupplier(supplierId);

            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Successfully Deleted");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getSupIdNameList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Supplier> supplierList = supplierRepository.findAllByOrderByNameAsc();
            List<IdNameDto> idNameDtoList = new ArrayList<>();
            IdNameDto idNameDto;
            for (Supplier supplier : supplierList) {
                idNameDto = new IdNameDto();
                idNameDto.setId(supplier.getId());
                idNameDto.setName(supplier.getName());

                idNameDtoList.add(idNameDto);
            }
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Supplier List");
            responseDto.setData(idNameDtoList);
        } catch (Exception ex){
            ex.printStackTrace();
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
