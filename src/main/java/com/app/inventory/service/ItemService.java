package com.app.inventory.service;

import com.app.inventory.dto.ItemDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.UserDto;
import com.app.inventory.model.Item;
import com.app.inventory.model.User;
import com.app.inventory.repository.ItemRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> getItemList(){
        List<Item> itemList = itemRepository.findAll();
        return itemList;
    }

    public ResponseEntity<?> createNewItem(ItemDto itemDto) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setQuantity(0.0);
        item.setPrice(itemDto.getPrice());
        item.setWarranty(itemDto.getWarranty());
        item.setCreatedDate(LocalDateTime.now());
        itemRepository.save(item);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Successfully Inserted");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateItem(int itemId,
                                        String itemName,
                                        double price,
                                        int warranty) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        Item item = itemOptional.get();

        item.setName(itemName);
        item.setPrice(price);
        item.setWarranty(warranty);
        itemRepository.save(item);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Successfully Updated");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
