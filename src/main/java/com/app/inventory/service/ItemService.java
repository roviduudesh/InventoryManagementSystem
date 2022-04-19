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

import static com.app.inventory.service.Common.EXCEPTION;
import static com.app.inventory.service.Common.SUCCESS;

@Data
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final StockRepository stockRepository;
    private final OrderItemRepository orderItemRepository;

    public ResponseEntity<?> getItemList(){
        try {
            List<Item> itemList = itemRepository.findAllByOrderByNameAsc();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, itemList), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> createNewItem(ItemDto itemDto) {
        try {
            List<Item> itemList = itemRepository.findAll();

            Optional<Item> itemOptional = itemList.stream()
                    .filter(i -> i.getName().equalsIgnoreCase(itemDto.getName().trim()))
                    .findFirst();

            if(itemOptional.isPresent()){
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), "Item Name Exists", null), HttpStatus.NOT_ACCEPTABLE);
            } else {
                Item item = new Item();
                item.setName(itemDto.getName());
                item.setQuantity(itemDto.getQuantity());
                item.setPrice(itemDto.getPrice());
                item.setWarranty(itemDto.getWarranty());
                item.setCreatedDate(LocalDateTime.now());
                itemRepository.save(item);

                return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
            }
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> updateItem(int itemId, ItemDto itemDto) {
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
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), "Stocks/Invoices Exists !!!", null), HttpStatus.NOT_ACCEPTABLE);
                } else if(itemName.isPresent()){
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), "Item Name Exists", null), HttpStatus.NOT_ACCEPTABLE);
                } else{
                    item.setName(itemDto.getName());
                    item.setPrice(itemDto.getPrice());
                    item.setQuantity(itemDto.getQuantity());
                    item.setWarranty(itemDto.getWarranty());

                    itemRepository.save(item);
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
                }
            } else{
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Item Not Found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    boolean checkItem(int itemId){
        List<Stock> stockList = stockRepository.findByItem_Id(itemId);
        List<OrderItem> orderItemList = orderItemRepository.findByOrderItemKey_ItemId(itemId);
        return stockList.size() > 0 || orderItemList.size() > 0 ? true : false;
    }

    public ResponseEntity<?> deleteItem(int itemId) {
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);
            if (itemOptional.isPresent()) {
                boolean checkItem = checkItem(itemId);
                if (checkItem) {
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), "Unable to Delete, Stocks/Invoices Exists !!!", null), HttpStatus.NOT_ACCEPTABLE);
                } else {
                    itemRepository.deleteById(itemId);
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Item Not Found", null), HttpStatus.NO_CONTENT);
            }
        } catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), SUCCESS, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> getItemIdNameList() {
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
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, idNameDtoList), HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> getItemIdQtyPriceList() {
        try {
            List<Item> itemList = itemRepository.findAllByQuantityGreaterThanOrderByNameAsc(0);
            List<IdQtyDtoPrice> idQtyDtoList = new ArrayList<>();
            IdQtyDtoPrice idQtyDto;
            for (Item item : itemList) {
                idQtyDto = new IdQtyDtoPrice();
                idQtyDto.setId(item.getId());
                idQtyDto.setQuantity(item.getQuantity());
                idQtyDto.setPrice(item.getPrice());
                idQtyDtoList.add(idQtyDto);
            }
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, idQtyDtoList), HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), EXCEPTION, null), HttpStatus.OK);
        }
    }
}
