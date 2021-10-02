package com.app.inventory.controller;

import com.app.inventory.dto.ItemDto;
import com.app.inventory.model.Item;
import com.app.inventory.service.ItemService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping(path = "/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/all")
    public List<Item> getItemList(){
        return itemService.getItemList();
    }

    @PostMapping
    public ResponseEntity<?> addNewItem(@RequestBody ItemDto itemDto){
        ResponseEntity<?> responseEntity = itemService.createNewItem(itemDto);
        return responseEntity;
    }

    @PutMapping(path = "{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable("itemId") int itemId,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) double price,
                                            @RequestParam(required = false) int warranty){
        ResponseEntity<?> responseEntity = itemService.updateItem(itemId, name, price, warranty);
        return responseEntity;
    }
}
