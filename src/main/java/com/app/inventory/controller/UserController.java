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

@CrossOrigin(origins = "*")
@Data
@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getUserList(){
        return userService.getUserList();
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam("userId") int userId){
        return userService.getUserProfile(userId);
    }

    @PostMapping
    public ResponseEntity<?> addNewUser(@RequestBody UserDto userDto){
        ResponseEntity<?> responseDto = userService.createNewUser(userDto);
        return responseDto;
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") int userId,
                                        @RequestBody UserDto userDto) {
        ResponseEntity<?> responseEntity = userService.updateUser(userDto);
        return responseEntity;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        ResponseEntity<?> responseDto = userService.login(loginDto);
        return responseDto;
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") int userId){
        ResponseEntity<?> responseEntity = userService.deleteUser(userId);
        return responseEntity;
    }
}
