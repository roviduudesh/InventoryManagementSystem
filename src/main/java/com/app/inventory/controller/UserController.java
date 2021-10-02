package com.app.inventory.controller;

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
    public List<User> getSupplierList(){
        return userService.getUserList();
    }

    @PostMapping
    public ResponseEntity<?> addNewSupplier(@RequestBody UserDto userDto){
        ResponseEntity<?> responseDto = userService.createNewUser(userDto);
        return responseDto;
    }

    @PutMapping(path = "{supplierId}")
    public ResponseEntity<?> updateSupplier(@PathVariable("supplierId") int supplierId,
                                            @RequestParam(required = false) String firstName,
                                            @RequestParam(required = false) String lastName,
                                            @RequestParam(required = false) String contact){
        ResponseEntity<?> responseEntity = userService.updateUser(supplierId, firstName, lastName, contact);
        return responseEntity;
    }


}
