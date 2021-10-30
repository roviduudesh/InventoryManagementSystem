package com.app.inventory.service;

import com.app.inventory.dto.IdNameDto;
import com.app.inventory.dto.ItemDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.UserDto;
import com.app.inventory.model.Item;
import com.app.inventory.model.Stock;
import com.app.inventory.model.Supplier;
import com.app.inventory.model.User;
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
        try {
            Item item = new Item();
            item.setName(itemDto.getName());
            item.setQuantity(itemDto.getQuantity());
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

    public ResponseEntity<?> updateItem(int itemId, ItemDto itemDto) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);


            if(itemOptional.isPresent()){
                Item item = itemOptional.get();

                if(checkItem(itemId)) {
                    status = HttpStatus.METHOD_NOT_ALLOWED.value();
                    message = "Updated, Stocks/Invoices Exists !!!";
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
        List<Item> itemList = orderItemRepository.findByOrderItemKey_ItemId(itemId);
        return stockList.size() > 0 || itemList.size() > 0 ? true : false;
    }

    public ResponseEntity<?> deleteItem(int itemId) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);
            if (itemOptional.isPresent()) {
                Item item = itemOptional.get();

                if (checkItem(itemId)) {
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
            List<Item> itemList = itemRepository.findAllByOrderByNameAsc();
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
}
