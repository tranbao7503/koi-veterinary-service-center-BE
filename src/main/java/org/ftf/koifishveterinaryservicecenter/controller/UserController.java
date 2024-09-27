package org.ftf.koifishveterinaryservicecenter.controller;


import org.ftf.koifishveterinaryservicecenter.dto.UserDto;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.mapper.UserMapper;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> getProfile() {
        UserDto dto = userService.getUserProfile(1);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/veterinarians")
    public ResponseEntity<List<UserDto>> getAllVeterianrians() {
        List<User> users = userService.getAllVeterinarians();
        if(users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<UserDto> userDtos = users.stream()
                    .map(user -> UserMapper.INSTANCE.convertEntityToDtoIgnoreAddress(user))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDtos, HttpStatus.OK);
        }
    }

}
