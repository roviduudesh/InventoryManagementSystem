package com.app.inventory.repository;

import com.app.inventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    void deleteById(int itemId);

    Optional<Item> findById(int id);

    List<Item> findAllByOrderByNameAsc();

    List<Item> findAllByQuantityGreaterThanOrderByNameAsc(double quantity);

}
