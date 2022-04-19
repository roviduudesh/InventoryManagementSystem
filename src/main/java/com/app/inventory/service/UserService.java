package com.app.inventory.service;

import com.app.inventory.dto.LoginDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.UserDto;
import com.app.inventory.model.Order;
import com.app.inventory.model.Stock;
import com.app.inventory.model.Supplier;
import com.app.inventory.model.User;
import com.app.inventory.repository.OrderRepository;
import com.app.inventory.repository.StockRepository;
import com.app.inventory.repository.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.inventory.service.Common.EXCEPTION;
import static com.app.inventory.service.Common.SUCCESS;

@Data
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;

    public ResponseEntity<?> getUserList(){
        try {
            List<User> userList = userRepository.findAllByOrderByFirstNameAsc();
            userList.forEach(u -> u.setPassword("********"));
            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, userList), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> createNewUser(UserDto userDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        try {
            List<User> userList = userRepository.findAll();
            Optional<User> userOptional = userList.stream()
                    .filter(u -> u.getUserName().equalsIgnoreCase(userDto.getUserName().trim()))
                    .findFirst();
            if (userOptional.isPresent()) {
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), "Username Exists", null), HttpStatus.NOT_ACCEPTABLE);
            } else {
                User user = new User();
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                user.setUserName(userDto.getUserName());
                user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
                user.setContact(userDto.getContact());
                user.setLevel(userDto.getLevel());
                userRepository.save(user);
                return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
            }
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> updateUser(UserDto userDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        try {
            Optional<User> userOptional = userRepository.findById(userDto.getId());
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                List<User> userList = userRepository.findAll();
                Optional<User> userName = userList.stream()
                        .filter(u -> u.getId() != userDto.getId() && u.getUserName().equalsIgnoreCase(userDto.getUserName().trim()))
                        .findFirst();

                if (userName.isPresent()) {
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), "Username Exists", null), HttpStatus.NOT_ACCEPTABLE);
                } else {
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setContact(userDto.getContact());
                    user.setUserName(userDto.getUserName());
                    user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
                    user.setLevel(userDto.getLevel());
                    userRepository.save(user);
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
                }
            } else{
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "User Not Found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<?> login(LoginDto loginDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        try {
            Optional<User> userOptional = userRepository.findByUserName(loginDto.getUserName());
            if (userOptional.isPresent()) {
                boolean match = bCryptPasswordEncoder.matches(loginDto.getPassword(), userOptional.get().getPassword());

                if (match){
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, userOptional.get()), HttpStatus.OK);
                } else{
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Invalid Password", null), HttpStatus.NO_CONTENT);
                }
            } else {
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "User Not Found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getUserProfile(int userId) {
        List<User> userList = new ArrayList<>();
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            User user = userOptional.get();
            user.setPassword("********");
            userList.add(user);

            return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, userList), HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> deleteUser(int userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                List<Stock> stockList = stockRepository.findAllByUser_Id(userId);
                List<Order> orderList = orderRepository.findAllByUser_Id(userId);
                if(stockList.size() > 0 || orderList.size() > 0){
                   return new ResponseEntity<>(new ResponseDto(HttpStatus.NOT_ACCEPTABLE.value(), "Stock / Order Exists", null), HttpStatus.NOT_ACCEPTABLE);
                } else {
                    userRepository.delete(userOptional.get());
                    return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(), SUCCESS, null), HttpStatus.OK);
                }
            } else{
                return new ResponseEntity<>(new ResponseDto(HttpStatus.NO_CONTENT.value(), "Supplier not Found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), EXCEPTION, null), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
