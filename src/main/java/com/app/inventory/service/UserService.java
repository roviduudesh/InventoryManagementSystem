package com.app.inventory.service;

import com.app.inventory.dto.LoginDto;
import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.UserDto;
import com.app.inventory.model.Supplier;
import com.app.inventory.model.User;
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
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<?> getUserList(){
        ResponseDto responseDto = new ResponseDto();
        try {
            List<User> userList = userRepository.findAll();
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
        int status;
        String message;
        try {
            if(checkUserName(userDto.getUserName())){
                status = HttpStatus.MULTI_STATUS.value();
                message = "Username Exists";
            } else {
                User user = new User();
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                user.setUserName(userDto.getUserName());
                user.setPassword(userDto.getPassword());
                user.setContact(userDto.getContact());
                userRepository.save(user);
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

    boolean checkUserName(String userName){
        Optional<User> userOptional = userRepository.findByUserName(userName);
        return userOptional.isPresent() ? true : false;
    }

    public ResponseEntity<?> updateUser(int userId,
                                        String firstName,
                                        String lastName,
                                        String contact) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()){
                User user = userOptional.get();

                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setContact(contact);
                userRepository.save(user);
                status = HttpStatus.OK.value();
                message = "Successfully Updated";
            } else{
                status = HttpStatus.NO_CONTENT.value();
                message = "User Not Found";
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

    public ResponseEntity<?> login(LoginDto loginDto) {
        ResponseDto responseDto = new ResponseDto();
        int status;
        String message;
        try {
            Optional<User> user = userRepository.findByUserName(loginDto.getUserName());
            if (user.isPresent()) {
                status = HttpStatus.OK.value();
                message = "Success";
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
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
