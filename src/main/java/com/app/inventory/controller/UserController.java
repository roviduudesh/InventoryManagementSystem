package com.app.inventory.controller;

import com.app.inventory.dto.LoginDto;
import com.app.inventory.dto.SupplierDto;
import com.app.inventory.dto.UserDto;
import com.app.inventory.model.Supplier;
import com.app.inventory.model.User;
import com.app.inventory.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getUserList(){
        return userService.getUserList();
    }

    @PostMapping
    public ResponseEntity<?> addNewUser(@RequestBody UserDto userDto){
        ResponseEntity<?> responseDto = userService.createNewUser(userDto);
        return responseDto;
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") int userId,
                                            @RequestParam(required = false) String firstName,
                                            @RequestParam(required = false) String lastName,
                                            @RequestParam(required = false) String contact){
        ResponseEntity<?> responseEntity = userService.updateUser(userId, firstName, lastName, contact);
        return responseEntity;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        ResponseEntity<?> responseDto = userService.login(loginDto);
        return responseDto;
    }
}
