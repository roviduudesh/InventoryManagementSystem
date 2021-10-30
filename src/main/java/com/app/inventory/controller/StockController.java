package com.app.inventory.controller;

import com.app.inventory.dto.StockDto;
import com.app.inventory.service.StockService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Data
@RestController
@RequestMapping(path = "api/v1/stock")
public class StockController {

    private final StockService stockService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getStockList(){
        return stockService.getStockList();
    }

    @PostMapping
    public ResponseEntity<?> addNewStock(@RequestBody StockDto stockDto){
        ResponseEntity<?> responseDto = stockService.createNewStock(stockDto);
        return responseDto;
    }

    @PutMapping(path = "{stockId}")
    public ResponseEntity<?> updateSupplier(@PathVariable("stockId") int stockId,
                                            @RequestBody StockDto stockDto){
        ResponseEntity<?> responseEntity = stockService.updateStock(stockId, stockDto);
        return responseEntity;
    }
}
