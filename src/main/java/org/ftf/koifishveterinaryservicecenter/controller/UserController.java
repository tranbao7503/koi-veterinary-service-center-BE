package org.ftf.koifishveterinaryservicecenter.controller;


import org.ftf.koifishveterinaryservicecenter.dto.AddressDto;
import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenicationException;
import org.ftf.koifishveterinaryservicecenter.mapper.AddressMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.UserMapper;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam Integer userId) {

        Integer userIdFromToken = 1;  // the userId takes from Authentication object in SecurityContext
        User user = userService.getUserProfile(userId);
        UserDTO userDto = UserMapper.INSTANCE.convertEntityToDto(user);
        return ResponseEntity.ok(userDto);
    }


    @GetMapping("/veterinarians")
    public ResponseEntity<List<UserDTO>> getAllVeterianrians() {
        List<User> users = userService.getAllVeterinarians();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<UserDTO> userDtos = users.stream()
                    .map(UserMapper.INSTANCE::convertEntityToDtoIgnoreAddress)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDtos, HttpStatus.OK);
        }
    }

    @PutMapping("/address")
    public ResponseEntity<?> updateAddressForCustomer(@RequestParam Integer userId, @RequestBody AddressDto addressFromRequest) {

        Address convertedAddress = AddressMapper.INSTANCE.convertDtoToEntity(addressFromRequest);

        Integer userIdFromToken = 1; // the userId takes from Authentication object in SecurityContext

        // check(userIdFromToken, userId)

        User updatedCustomer = userService.updateAddress(userId, convertedAddress);
        UserDTO userDto = UserMapper.INSTANCE.convertEntityToDto(updatedCustomer);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestParam Integer userId, @RequestBody UserDTO userFromRequest) {


        try {
            User convertedCustomer = UserMapper.INSTANCE.convertDtoToEntity(userFromRequest);
            Integer userIdFromToken = 1; // the userId takes from Authentication object in SecurityContext

            if (!userId.equals(userIdFromToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // check(userIdFromToken, userId)
            User updatedCustomer = userService.updateUserProfile(userId, convertedCustomer);
            UserDTO userDto = UserMapper.INSTANCE.convertEntityToDto(updatedCustomer);
            return ResponseEntity.ok(userDto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }


    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {
        List<User> customers = userService.getAllCustomers();

        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<UserDTO> userDTOs = customers.stream()
                    .map(UserMapper.INSTANCE::convertEntityToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        }
    }

    @GetMapping("/staffssandveterinarians")
    public ResponseEntity<?> getAllStaffsAndVeterinarians() {
        List<User> staffsAndVeterninarians = userService.getAllStaffsAndVeterinarians();

        if (staffsAndVeterninarians.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<UserDTO> userDTOS = staffsAndVeterninarians.stream()
                    .map(UserMapper.INSTANCE::convertEntityToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDTOS, HttpStatus.OK);
        }
    }

    @PostMapping("signup")
    public ResponseEntity<?> signUp(@RequestParam String username, @RequestParam String password, @RequestParam String first_name, @RequestParam String last_name) {
        try {
            userService.signUp(username, password, first_name, last_name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenicationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }
}
