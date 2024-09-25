package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/manager")
public class ManagerController {

    private ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("staffs")
    public ResponseEntity<List<UserDTO>> getAllStaff(){
        List<UserDTO> staffs = managerService.getAllUser(4);

        if(staffs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(staffs,HttpStatus.OK);
        }
    }

    @GetMapping("veterinarians")
    public ResponseEntity<List<UserDTO>> getAllReterinarians(){
        List<UserDTO> veterinarians = managerService.getAllUser(3);

        if(veterinarians.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(veterinarians,HttpStatus.OK);
        }
    }
}
