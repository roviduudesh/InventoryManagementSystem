package com.app.inventory.service;

import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.StockDto;
import com.app.inventory.model.Item;
import com.app.inventory.model.Stock;
import com.app.inventory.model.Supplier;
import com.app.inventory.model.User;
import com.app.inventory.repository.ItemRepository;
import com.app.inventory.repository.StockRepository;
import com.app.inventory.repository.SupplierRepository;
import com.app.inventory.repository.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<Stock> getStockList(){
        List<Stock> stockList = stockRepository.findAll();
        return stockList;
    }

    public ResponseEntity<?> createNewStock(StockDto stockDto) {
        Stock stock = new Stock();

        stock.setStockDate(stockDto.getStockDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        stock.setQuantity(stockDto.getQuantity());

        Optional<Supplier> supplierOptional = supplierRepository.findById(stockDto.getSupplierId());
        Optional<Item> itemOptional = itemRepository.findById(stockDto.getItemId());
        Optional<User> userOptional = userRepository.findById(stockDto.getUserId());

        stock.setSupplier(supplierOptional.get());
        stock.setItem(itemOptional.get());
        stock.setUser(userOptional.get());

        stockRepository.save(stock);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Successfully Inserted");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
