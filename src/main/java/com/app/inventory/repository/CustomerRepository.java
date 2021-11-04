package com.app.inventory.repository;

import com.app.inventory.model.Customer;
import com.app.inventory.model.Supplier;
import com.app.inventory.model.SupplierContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findTopByOrderByIdDesc();

    @Modifying
    @Query("delete from Customer c where c.id=:cusId")
    void deleteCustomer(@Param("cusId") int cusId);

    List<Customer> findAllByOrderByFirstNameAsc();
}
