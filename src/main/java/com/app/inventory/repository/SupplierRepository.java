package com.app.inventory.repository;

import com.app.inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findTopByOrderByIdDesc();
}
