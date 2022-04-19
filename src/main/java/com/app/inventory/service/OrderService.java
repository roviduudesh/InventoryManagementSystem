package com.app.inventory.service;

import com.app.inventory.dto.order.NewOrderDto;
import com.app.inventory.dto.order.OrderItemDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.order.ViewOrderDto;
import com.app.inventory.model.*;
import com.app.inventory.model.key.OrderItemKey;
import com.app.inventory.repository.*;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.app.inventory.service.Common.EXCEPTION;
import static com.app.inventory.service.Common.SUCCESS;

@Data
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    public ResponseEntity<?> getOrderList(){
        ViewOrderDto viewOrderDto = null;
        List<ViewOrderDto> viewOrderDtoList = new ArrayList<>();
        List<OrderItemDto> orderItemDtoList = null;
        OrderItemDto orderItemDto;
        try {
            List<Order> orderList = orderRepository.findAll();
            for (Order order : orderList) {
                viewOrderDto = new ViewOrderDto();
                viewOrderDto.setOrderId(order.getId());
                viewOrderDto.setOrderDate(order.getOrderDate());
                viewOrderDto.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
                viewOrderDto.setTotalAmount(order.getOrderItemList().stream().mapToDouble(i -> i.getAmount()).sum());
                orderItemDtoList = new ArrayList<>();
                for (OrderItem orderItem : order.getOrderItemList()) {
                    Optional<Item> itemOptional = itemRepository.findById(orderItem.getOrderItemKey().getItemId());

                    Item item = itemOptional.get();

                    orderItemDto = new OrderItemDto();
                    orderItemDto.setItemId(item.getId());
                    orderItemDto.setItemName(item.getName());
                    orderItemDto.setQuantity(orderItem.getQuantity());
                    orderItemDto.setAmount(orderItem.getAmount());
                    orderItemDtoList.add(orderItemDto);
                }

                viewOrderDto.setOrderItemList(orderItemDtoList);
                viewOrderDtoList.add(viewOrderDto);
            }
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, viewOrderDtoList), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> getOrderItemList(){
        try {
            List<OrderItem> orderItemList = orderItemRepository.findAll();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, orderItemList), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> createNewOrder(List<NewOrderDto> orderDtoList) {
        ResponseDto responseDto = new ResponseDto();
        int customerId;
        int userId;
        try {
            Order order = new Order();
            String orderId = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
            order.setId(orderId);
            order.setOrderDate(LocalDateTime.now());
            customerId = orderDtoList.get(0).getCustomerId();
            userId = orderDtoList.get(0).getUserId();

            Optional<Customer> customerOptional = customerRepository.findById(customerId);
            Optional<User> userOptional = userRepository.findById(userId);

            if(!customerOptional.isPresent()){
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Customer Not Found", null), HttpStatus.NO_CONTENT);
            } else if (!userOptional.isPresent()){
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "User Not Found", null), HttpStatus.NO_CONTENT);
            } else {
                order.setCustomer(customerOptional.get());
                order.setUser(userOptional.get());
                orderRepository.save(order);

                OrderItem orderItem;
                OrderItemKey orderItemKey;
                Optional<Item> itemOptional;
                Item item;
                for (NewOrderDto newOrderDto : orderDtoList) {
                    int itemId = newOrderDto.getItemId();
                    double orderQuantity = newOrderDto.getQuantity();
                    itemOptional = itemRepository.findById(itemId);
                    item = itemOptional.get();

                    item.setQuantity(item.getQuantity() - orderQuantity);
                    itemRepository.save(item);

                    orderItemKey = new OrderItemKey();
                    orderItemKey.setOrderId(order.getId());
                    orderItemKey.setItemId(itemId);

                    orderItem = new OrderItem();
                    orderItem.setOrderItemKey(orderItemKey);
                    orderItem.setQuantity(orderQuantity);
                    orderItem.setAmount(newOrderDto.getAmount());
                    orderItemRepository.save(orderItem);
                }
                return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
