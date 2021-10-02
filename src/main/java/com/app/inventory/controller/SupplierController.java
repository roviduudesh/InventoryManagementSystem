package com.app.inventory.controller;

import com.app.inventory.dto.SupplierDto;
import com.app.inventory.model.Supplier;
import com.app.inventory.service.SupplierService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping(path = "api/v1/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping(value = "/all")
    public List<Supplier> getSupplierList(){
        return supplierService.getSupplierList();
    }

    @PostMapping
    public ResponseEntity<?> addNewSupplier(@RequestBody SupplierDto newSupplierDto){
        ResponseEntity<?> responseDto = supplierService.createNewSupplier(newSupplierDto);
        return responseDto;
    }

    @PutMapping(path = "{supplierId}")
    public ResponseEntity<?> updateSupplier(@PathVariable("supplierId") int supplierId,
                                            @RequestBody SupplierDto supplierDto){
        ResponseEntity<?> responseEntity = supplierService.updateSupplier(supplierId, supplierDto);
        return responseEntity;
    }
}
