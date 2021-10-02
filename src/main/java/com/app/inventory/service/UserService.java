package com.app.inventory.service;

import com.app.inventory.dto.ResponseDto;
import com.app.inventory.dto.UserDto;
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

    public List<User> getUserList(){
        List<User> userList = userRepository.findAll();
        return userList;
    }

    public ResponseEntity<?> createNewUser(UserDto userDto) {
        User user = new User();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setContact(userDto.getContact());
        userRepository.save(user);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Successfully Inserted");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(int userId,
                                        String firstName,
                                        String lastName,
                                        String contact) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setContact(contact);
        userRepository.save(user);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(1);
        responseDto.setMessage("Successfully Updated");
        responseDto.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

}
