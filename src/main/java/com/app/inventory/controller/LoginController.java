package com.app.inventory.controller;

import com.app.inventory.dto.ItemDto;
import com.app.inventory.dto.LoginDto;
import com.app.inventory.service.UserService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Data
@RestController
@RequestMapping(path = "/api/v1/login")
public class LoginController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        ResponseEntity<?> responseEntity = userService.login(loginDto);
        return responseEntity;
    }
}
