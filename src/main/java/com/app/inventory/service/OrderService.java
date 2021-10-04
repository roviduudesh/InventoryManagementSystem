package com.app.inventory.service;

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

    public ResponseEntity<?> getOrderList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<Order> orderList = orderRepository.findAll();
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Order List");
            responseDto.setData(orderList);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getOrderItemList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<OrderItem> orderItemList = orderItemRepository.findAll();
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Order-Item List");
            responseDto.setData(orderItemList);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createNewOrder(OrderDto orderDto) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());

            Optional<Customer> customerOptional = customerRepository.findById(orderDto.getCustomerId());
            Optional<User> userOptional = userRepository.findById(orderDto.getUserId());

            if(!customerOptional.isPresent()){
                status = HttpStatus.NO_CONTENT.value();
                message = "Customer Not Found";
            } else if (!userOptional.isPresent()){
                status = HttpStatus.NO_CONTENT.value();
                message = "User Not Found";
            } else {
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
                status = HttpStatus.OK.value();
                message = "Successfully Inserted";
            }
            responseDto.setStatus(status);
            responseDto.setMessage(message);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
