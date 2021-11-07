package com.app.inventory.service;

import com.app.inventory.dto.*;
import com.app.inventory.model.*;
import com.app.inventory.repository.ItemRepository;
import com.app.inventory.repository.OrderItemRepository;
import com.app.inventory.repository.StockRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final StockRepository stockRepository;
    private final OrderItemRepository orderItemRepository;

    public ResponseEntity<?> getItemList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Item> itemList = itemRepository.findAllByOrderByNameAsc();
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
        int status;
        String message;
        try {
            List<Item> itemList = itemRepository.findAll();

            Optional<Item> itemOptional = itemList.stream()
                    .filter(i -> i.getName().equalsIgnoreCase(itemDto.getName().trim()))
                    .findFirst();

            if(itemOptional.isPresent()){
                status = HttpStatus.NOT_ACCEPTABLE.value();
                message = "Item Name Exists";
            } else {
                Item item = new Item();
                item.setName(itemDto.getName());
                item.setQuantity(itemDto.getQuantity());
                item.setPrice(itemDto.getPrice());
                item.setWarranty(itemDto.getWarranty());
                item.setCreatedDate(LocalDateTime.now());
                itemRepository.save(item);

                status = HttpStatus.OK.value();
                message = "Successfully Inserted";
            }
        } catch (Exception ex){
            status = HttpStatus.EXPECTATION_FAILED.value();
            message = "Technical Failure";
        }
        responseDto.setStatus(status);
        responseDto.setMessage(message);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateItem(int itemId, ItemDto itemDto) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);
            List<Item> itemList = itemRepository.findAll();

            Optional<Item> itemName = itemList.stream()
                    .filter(i -> i.getId() != itemId && i.getName().equalsIgnoreCase(itemDto.getName().trim()))
                    .findFirst();

            if(itemOptional.isPresent()){
                Item item = itemOptional.get();
                boolean checkItem = checkItem(itemId);

                if(checkItem) {
                    status = HttpStatus.METHOD_NOT_ALLOWED.value();
                    message = "Stocks/Invoices Exists !!!";
                } else if(itemName.isPresent()){
                    status = HttpStatus.NOT_ACCEPTABLE.value();
                    message = "Item Name Exists";
                } else{
                    item.setName(itemDto.getName());
                    item.setPrice(itemDto.getPrice());
                    item.setQuantity(itemDto.getQuantity());
                    item.setWarranty(itemDto.getWarranty());

                    itemRepository.save(item);
                    status = HttpStatus.OK.value();
                    message = "Successfully Updated";
                }
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

    boolean checkItem(int itemId){
        List<Stock> stockList = stockRepository.findByItem_Id(itemId);
        List<OrderItem> orderItemList = orderItemRepository.findByOrderItemKey_ItemId(itemId);
        return stockList.size() > 0 || orderItemList.size() > 0 ? true : false;
    }

    public ResponseEntity<?> deleteItem(int itemId) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);
            if (itemOptional.isPresent()) {
                boolean checkItem = checkItem(itemId);
                if (checkItem) {
                    status = HttpStatus.METHOD_NOT_ALLOWED.value();
                    message = "Unable to Delete, Stocks/Invoices Exists !!!";
                } else {
                    itemRepository.deleteById(itemId);
                    status = HttpStatus.OK.value();
                    message = "Successfully Deleted";
                }
            } else {
                status = HttpStatus.NO_CONTENT.value();
                message = "Item Not Found";
            }
        } catch(Exception ex){
            ex.printStackTrace();
            status = HttpStatus.EXPECTATION_FAILED.value();
            message = "Technical Failure";
        }
        responseDto.setStatus(status);
        responseDto.setMessage(message);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getItemIdNameList() {
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Item> itemList = itemRepository.findAllByQuantityGreaterThanOrderByNameAsc(0);
            List<IdNameDto> idNameDtoList = new ArrayList<>();
            IdNameDto idNameDto;
            for (Item item : itemList) {
                idNameDto = new IdNameDto();
                idNameDto.setId(item.getId());
                idNameDto.setName(item.getName());

                idNameDtoList.add(idNameDto);
            }
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Supplier List");
            responseDto.setData(idNameDtoList);
        } catch (Exception ex){
            ex.printStackTrace();
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getItemIdQtyList() {
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Item> itemList = itemRepository.findAllByQuantityGreaterThanOrderByNameAsc(0);
            List<IdQtyDto> idQtyDtoList = new ArrayList<>();
            IdQtyDto idQtyDto;
            for (Item item : itemList) {
                idQtyDto = new IdQtyDto();
                idQtyDto.setId(item.getId());
                idQtyDto.setQuantity(item.getQuantity());

                idQtyDtoList.add(idQtyDto);
            }
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Supplier List");
            responseDto.setData(idQtyDtoList);
        } catch (Exception ex){
            ex.printStackTrace();
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
