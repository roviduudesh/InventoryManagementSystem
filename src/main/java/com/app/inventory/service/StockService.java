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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getStockList(){
        ResponseDto responseDto = new ResponseDto();
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
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Stock List");
            responseDto.setData(viewStockDtoList);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createNewStock(StockDto stockDto) {
        ResponseDto responseDto = new ResponseDto();
        Optional<Supplier> supplierOptional = supplierRepository.findById(stockDto.getSupplierId());
        Optional<Item> itemOptional = itemRepository.findById(stockDto.getItemId());
//        Optional<User> userOptional = userRepository.findById(createStockDto.getUserId());
        int status;
        String message;
        try {
            if(!supplierOptional.isPresent()){
                status = HttpStatus.NO_CONTENT.value();
                message = "Supplier Not Found";
            } else if(!itemOptional.isPresent()){
                status = HttpStatus.NO_CONTENT.value();
                message = "Item Not Found";
            } /*else if(!userOptional.isPresent()){
                status = HttpStatus.NO_CONTENT.value();
                message = "User Not Found";
            } */else {
                Stock stock = new Stock();
//                stock.setStockDate(stockDto.getStockDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                stock.setStockDate(stockDto.getStockDate());
                stock.setQuantity(stockDto.getQuantity());

                Item item = itemOptional.get();
                stock.setItem(item);

                item.setQuantity(item.getQuantity() + stockDto.getQuantity());
                itemRepository.save(item);

                stock.setSupplier(supplierOptional.get());
                stockRepository.save(stock);

                status = HttpStatus.OK.value();
                message = "Successfully Inserted";
            }
            responseDto.setStatus(status);
            responseDto.setMessage(message);
        } catch (Exception ex){
            ex.printStackTrace();
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateStock(int stockId, StockDto stockDto) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<Stock> stockOptional = stockRepository.findById(stockId);
            if(stockOptional.isPresent()){
                Stock stock = stockOptional.get();

                stock.setStockDate(stockDto.getStockDate());

                stockRepository.save(stock);
                status = HttpStatus.OK.value();
                message = "Successfully Updated";
            } else{
                status = HttpStatus.NO_CONTENT.value();
                message = "Stock Not Found";
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
