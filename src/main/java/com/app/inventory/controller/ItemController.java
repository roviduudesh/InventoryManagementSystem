package com.app.inventory.controller;

import com.app.inventory.dto.ItemDto;
import com.app.inventory.model.Item;
import com.app.inventory.service.ItemService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@Data
@RestController
@RequestMapping(path = "/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getItemList(){
        return itemService.getItemList();
    }

    @GetMapping(value = "/item_id_name")
    public ResponseEntity<?> getItemIdName(){
        ResponseEntity<?> responseDto =  itemService.getItemIdNameList();
        return responseDto;
    }

    @GetMapping(value = "/item_id_qty")
    public ResponseEntity<?> getItemIdQty(){
        ResponseEntity<?> responseDto =  itemService.getItemIdQtyList();
        return responseDto;
    }

    @PostMapping
    public ResponseEntity<?> addNewItem(@RequestBody ItemDto itemDto){
        ResponseEntity<?> responseEntity = itemService.createNewItem(itemDto);
        return responseEntity;
    }

    @PutMapping(path = "{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable("itemId") int itemId, @RequestBody ItemDto itemDto){
        ResponseEntity<?> responseEntity = itemService.updateItem(itemId, itemDto);
        return responseEntity;
    }

    @DeleteMapping(path = "{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable("itemId") int itemId){
        ResponseEntity<?> responseEntity = itemService.deleteItem(itemId);
        return responseEntity;
    }
}
