package com.app.inventory.repository;

import com.app.inventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByOrderByNameAsc();

    void deleteById(int itemId);

}
