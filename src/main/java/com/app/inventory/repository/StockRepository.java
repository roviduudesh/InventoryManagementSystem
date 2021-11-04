package com.app.inventory.repository;

import com.app.inventory.model.Stock;
import com.app.inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByItem_Id(int itemId);
    List<Stock> findAllBySupplier(Supplier supplier);
}
