package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/manager")
public class ManagerController {

    private UserService userService;

    @Autowired
    public ManagerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("customers")
    public ResponseEntity<List<UserDTO>> getAllCustomers(){
        List<UserDTO> customers=userService.getAllCustomers();

        if(customers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(customers,HttpStatus.OK);
        }
    }



}
