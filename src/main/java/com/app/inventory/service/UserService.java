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

@Data
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;

    public ResponseEntity<?> getUserList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<User> userList = userRepository.findAllByOrderByFirstNameAsc();
            userList.forEach(u -> u.setPassword("********"));
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("User List");
            responseDto.setData(userList);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createNewUser(UserDto userDto) {
        ResponseDto responseDto = new ResponseDto();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        int status;
        String message;
        try {
            List<User> userList = userRepository.findAll();
            Optional<User> userOptional = userList.stream()
                    .filter(u -> u.getUserName().equalsIgnoreCase(userDto.getUserName().trim()))
                    .findFirst();
            if (userOptional.isPresent()) {
                status = HttpStatus.NOT_ACCEPTABLE.value();
                message = "Username Exists";
            } else {
                User user = new User();
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                user.setUserName(userDto.getUserName());
                user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
                user.setContact(userDto.getContact());
                user.setLevel(userDto.getLevel());
                userRepository.save(user);
                status = HttpStatus.OK.value();
                message = "Successfully Inserted";
            }
        } catch (Exception ex){
            status = HttpStatus.EXPECTATION_FAILED.value();
            message = "Technical Failure";
        }
        responseDto.setStatus(status);
        responseDto.setMessage(message);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(UserDto userDto) {
        ResponseDto responseDto = new ResponseDto();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        int status;
        String message;
        try {
            Optional<User> userOptional = userRepository.findById(userDto.getId());
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                List<User> userList = userRepository.findAll();
                Optional<User> userName = userList.stream()
                        .filter(u -> u.getId() != userDto.getId() && u.getUserName().equalsIgnoreCase(userDto.getUserName().trim()))
                        .findFirst();

                if (userName.isPresent()) {
                    status = HttpStatus.NOT_ACCEPTABLE.value();
                    message = "Username Exists";
                } else {
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setContact(userDto.getContact());
                    user.setUserName(userDto.getUserName());
                    user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
                    user.setLevel(userDto.getLevel());
                    userRepository.save(user);
                    status = HttpStatus.OK.value();
                    message = "Successfully Updated";
                }
            } else{
                status = HttpStatus.NO_CONTENT.value();
                message = "User Not Found";
            }
            responseDto.setStatus(status);
            responseDto.setMessage(message);
        } catch (Exception ex){
            status = HttpStatus.EXPECTATION_FAILED.value();
            message = "Technical Failure";
        }
        responseDto.setStatus(status);
        responseDto.setMessage(message);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> login(LoginDto loginDto) {
        ResponseDto responseDto = new ResponseDto();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        int status;
        String message;
        User user = null;
        try {
            Optional<User> userOptional = userRepository.findByUserName(loginDto.getUserName());
            if (userOptional.isPresent()) {
                boolean match = bCryptPasswordEncoder.matches(loginDto.getPassword(), userOptional.get().getPassword());

                if (match){
                    status = HttpStatus.OK.value();
                    message = "Success";
                    user = userOptional.get();
                } else{
                    status = HttpStatus.NO_CONTENT.value();
                    message = "Invalid Password";
                }
            } else {
                status = HttpStatus.NO_CONTENT.value();
                message = "User Not Found";
            }
        } catch (Exception ex){
            status = HttpStatus.EXPECTATION_FAILED.value();
            message = "Technical Failure";
        }
        responseDto.setStatus(status);
        responseDto.setMessage(message);
        responseDto.setData(user);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getUserProfile(int userId) {
        ResponseDto responseDto = new ResponseDto();
        List<User> userList = new ArrayList<>();
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            User user = userOptional.get();
            user.setPassword("********");
            userList.add(user);
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("User List");
            responseDto.setData(userList);
        } catch (Exception ex){
            responseDto.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            responseDto.setMessage("Technical Failure");
            responseDto.setData(null);
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUser(int userId) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                List<Stock> stockList = stockRepository.findAllByUser_Id(userId);
                List<Order> orderList = orderRepository.findAllByUser_Id(userId);
                if(stockList.size() > 0 || orderList.size() > 0){
                    status = HttpStatus.NOT_ACCEPTABLE.value();
                    message = "Stock / Order Exists";
                } else {
                    userRepository.delete(userOptional.get());
                    status = HttpStatus.OK.value();
                    message = "Successfully Deleted";
                }
            } else{
                status = HttpStatus.NO_CONTENT.value();
                message = "Supplier not Found";
            }
        } catch (Exception ex){
            ex.printStackTrace();
            status = HttpStatus.EXPECTATION_FAILED.value();
            message = ex.getMessage();
        }
        responseDto.setStatus(status);
        responseDto.setMessage(message);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
