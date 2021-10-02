package com.app.inventory.controller;

import com.app.inventory.dto.NewSupplierDto;
import com.app.inventory.model.Supplier;
import com.app.inventory.service.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping(value = "/all")
    public List<Supplier> getSupplierList(){
        return supplierService.getSupplierList();
    }

    @PostMapping
    public ResponseEntity<?> addNewSupplier(@RequestBody NewSupplierDto newSupplierDto){
        ResponseEntity<?> responseDto = supplierService.createNewSupplier(newSupplierDto);
        return responseDto;
    }
}
