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

    public ResponseEntity<?> getItemList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Item> itemList = itemRepository.findAll();
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Item list");
            responseDto.setData(itemList);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createNewItem(ItemDto itemDto) {
        ResponseDto responseDto = new ResponseDto();
        try {
            Item item = new Item();
            item.setName(itemDto.getName());
            item.setQuantity(0.0);
            item.setPrice(itemDto.getPrice());
            item.setWarranty(itemDto.getWarranty());
            item.setCreatedDate(LocalDateTime.now());
            itemRepository.save(item);
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Successfully Inserted");
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateItem(int itemId,
                                        String itemName,
                                        double price,
                                        int warranty) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);
            if(itemOptional.isPresent()){
                Item item = itemOptional.get();
                item.setName(itemName);
                item.setPrice(price);
                item.setWarranty(warranty);
                itemRepository.save(item);
                status = HttpStatus.OK.value();
                message = "Successfully Updated";
            } else{
                status = HttpStatus.NO_CONTENT.value();
                message = "Item Not Found";
            }
            responseDto.setStatus(status);
            responseDto.setMessage(message);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
