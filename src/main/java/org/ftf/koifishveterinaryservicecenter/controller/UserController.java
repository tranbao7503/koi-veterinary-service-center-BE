package org.ftf.koifishveterinaryservicecenter.controller;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.ftf.koifishveterinaryservicecenter.dto.*;
import org.ftf.koifishveterinaryservicecenter.dto.response.AuthenticationResponse;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.mapper.AddressMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.UserMapper;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
/*@RequiredArgsConstructor*/
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;


    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
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


    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequestDTO request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequestDTO request)
            throws ParseException {
        var result = authenticationService.introspect(request);
        if (result == null) {
            return ApiResponse.<IntrospectResponse>builder().code(404).build();
        }
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
