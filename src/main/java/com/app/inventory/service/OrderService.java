package com.app.inventory.service;

import com.app.inventory.dto.ItemDto;
import com.app.inventory.dto.OrderDto;
import com.app.inventory.dto.OrderItemDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.model.*;
import com.app.inventory.model.key.OrderItemKey;
import com.app.inventory.repository.CustomerRepository;
import com.app.inventory.repository.OrderItemRepository;
import com.app.inventory.repository.OrderRepository;
import com.app.inventory.repository.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public List<Order> getOrderList(){
        List<Order> orderList = orderRepository.findAll();
        return orderList;
    }

    public List<OrderItem> getOrderItemList(){
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        return orderItemList;
    }

    public ResponseEntity<?> createNewOrder(OrderDto orderDto) {
        Order order = new Order();

        order.setOrderDate(LocalDateTime.now());

        Optional<Customer> customerOptional = customerRepository.findById(orderDto.getCustomerId());
        Optional<User> userOptional = userRepository.findById(orderDto.getUserId());

        order.setCustomer(customerOptional.get());
        order.setUser(userOptional.get());
        orderRepository.save(order);

        order = orderRepository.findTopByOrderByIdDesc();

        OrderItem orderItem;
        OrderItemKey orderItemKey;
        for (OrderItemDto orderItemDto : orderDto.getOrderItemList()) {
            orderItemKey = new OrderItemKey();
            orderItemKey.setOrderId(order.getId());
            orderItemKey.setItemId(orderItemDto.getItemId());

            orderItem = new OrderItem();
            orderItem.setOrderItemKey(orderItemKey);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setAmount(orderItemDto.getAmount());
            orderItemRepository.save(orderItem);
        }


        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Successfully Inserted");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
