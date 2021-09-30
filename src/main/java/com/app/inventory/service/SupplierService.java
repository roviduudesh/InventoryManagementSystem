package com.app.inventory.service;

import com.app.inventory.model.Supplier;
import com.app.inventory.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getSupplierList(){
        List<Supplier> supplierList = supplierRepository.findAll();
        return supplierList;
    }


}
