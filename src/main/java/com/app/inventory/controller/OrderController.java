package com.app.inventory.controller;

import com.app.inventory.dto.order.NewOrderDto;
import com.app.inventory.model.Order;
import com.app.inventory.service.OrderService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@Data
@RestController
@RequestMapping(path = "api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getOrderList(){
        return orderService.getOrderList();
    }

    @PostMapping
    public ResponseEntity<?> addNewOrder(@RequestBody List<NewOrderDto> orderItemDtoList){
        ResponseEntity<?> responseEntity = orderService.createNewOrder(orderItemDtoList);
        return responseEntity;
    }
}
