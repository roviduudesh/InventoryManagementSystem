package com.app.inventory.service;

import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.StockDto;
import com.app.inventory.model.*;
import com.app.inventory.repository.ItemRepository;
import com.app.inventory.repository.StockRepository;
import com.app.inventory.repository.SupplierRepository;
import com.app.inventory.repository.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.app.inventory.service.Common.EXCEPTION;
import static com.app.inventory.service.Common.SUCCESS;

@Data
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getStockList(){
        StockDto viewStockDto;
        List<StockDto> viewStockDtoList = new ArrayList<>();
        try {
            List<Stock> stockList = stockRepository.findAll();
            for (Stock stock : stockList) {
                viewStockDto = new StockDto();
                viewStockDto.setId(stock.getId());
                viewStockDto.setStockDate(stock.getStockDate());
                viewStockDto.setQuantity(stock.getQuantity());
                viewStockDto.setSupplierId(stock.getSupplier().getId());
                viewStockDto.setSupplierName(stock.getSupplier().getName());
                viewStockDto.setItemId(stock.getItem().getId());
                viewStockDto.setItemName(stock.getItem().getName());
                viewStockDtoList.add(viewStockDto);
            }
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, viewStockDtoList), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> createNewStock(StockDto stockDto) {
        Optional<Supplier> supplierOptional = supplierRepository.findById(stockDto.getSupplierId());
        Optional<Item> itemOptional = itemRepository.findById(stockDto.getItemId());
        Optional<User> userOptional = userRepository.findById(stockDto.getUserId());

        try {
            if(!supplierOptional.isPresent()){
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Supplier Not Found", null), HttpStatus.NO_CONTENT);
            } else if(!itemOptional.isPresent()){
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Item Not Found", null), HttpStatus.NO_CONTENT);
            } else if(!userOptional.isPresent()){
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "User Not Found", null), HttpStatus.NO_CONTENT);
            } else {
                Stock stock = new Stock();
                stock.setStockDate(stockDto.getStockDate().atZone(ZoneId.systemDefault()).toLocalDateTime());
                stock.setStockDate(stockDto.getStockDate());
                stock.setQuantity(stockDto.getQuantity());
                stock.setUser(userOptional.get());

                Item item = itemOptional.get();
                stock.setItem(item);
                item.setQuantity(item.getQuantity() + stockDto.getQuantity());
                itemRepository.save(item);

                stock.setSupplier(supplierOptional.get());
                stockRepository.save(stock);

                return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> updateStock(int stockId, StockDto stockDto) {
        try {
            Optional<Stock> stockOptional = stockRepository.findById(stockId);
            if(stockOptional.isPresent()){
                Stock stock = stockOptional.get();
                stock.setStockDate(stockDto.getStockDate());

                stockRepository.save(stock);
                return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
            } else{
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Stock not found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
