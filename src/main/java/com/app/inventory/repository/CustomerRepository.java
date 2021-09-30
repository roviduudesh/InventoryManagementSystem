package com.app.inventory.repository;

import com.app.inventory.model.Customer;
import com.app.inventory.model.SupplierContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
