package com.app.inventory.repository;

import com.app.inventory.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByItem_Id(int itemId);
}
