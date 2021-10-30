package com.app.inventory.repository;

import com.app.inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findTopByOrderByIdDesc();

    @Modifying
    @Query("delete from Supplier s where s.id=:supId")
    void deleteSupplier(@Param("supId") int supId);

    List<Supplier> findAllByOrderByNameAsc();
}
