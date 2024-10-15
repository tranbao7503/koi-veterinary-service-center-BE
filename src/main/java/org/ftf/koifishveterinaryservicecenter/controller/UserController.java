package org.ftf.koifishveterinaryservicecenter.controller;


import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.ftf.koifishveterinaryservicecenter.dto.*;
import org.ftf.koifishveterinaryservicecenter.dto.response.AuthenticationResponse;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AddressNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.FeedbackNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.AddressMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.UserMapper;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.feedbackservice.FeedbackService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationServiceImpl;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final FeedbackService feedbackService;
    private final AppointmentService appointmentService;
    private final AuthenticationServiceImpl authenticationService;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, AuthenticationService authenticationService, FeedbackService feedbackService, AppointmentService appointmentService, AuthenticationServiceImpl authenticationService1) {
        this.userService = userService;
        this.userMapper = userMapper;

        this.feedbackService = feedbackService;
        this.appointmentService = appointmentService;
        this.authenticationService = authenticationService1;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam Integer userId) {

        Integer userIdFromToken = userId;  // the userId takes from Authentication object in SecurityContext
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
    public ResponseEntity<?> updateAddressForCustomer(@RequestParam Integer userId, @RequestBody AddressDTO addressFromRequest) {

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
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // Log thông tin về username và role
        log.info("Userid: {}", authentication.getName()); // Đúng cú pháp cho log.info
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("role: {}", grantedAuthority.getAuthority()));
        List<User> customers = userService.getAllCustomers();

        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<UserDTO> userDTOs = customers.stream().map(userMapper::convertEntityToDto).collect(Collectors.toList());
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        }
    }

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequestDTO request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequestDTO request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTOFromRequest) {
        try {
            String username = userDTOFromRequest.getUsername();
            String password = userDTOFromRequest.getPassword();
            String email = userDTOFromRequest.getEmail();
            String firstName = userDTOFromRequest.getFirstName();
            String lastName = userDTOFromRequest.getLastName();

            userService.signUp(username, password, email, firstName, lastName);
            return new ResponseEntity<>("Sign up successfully", HttpStatus.OK);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Update avatar of user
     * Actors: Customer, Manager
     * */
    @PreAuthorize("hasAuthority('CUS')")
    @PutMapping("/avatar")
    public ResponseEntity<?> updateUserAvatar(@RequestParam("user_id") Integer userId, @RequestParam("image") MultipartFile image) {
        try {
            User user = userService.updateUserAvatar(userId, image);
            UserDTO userDto = UserMapper.INSTANCE.convertEntityToDtoIgnoreAddress(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (FeedbackNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Actors: Manager
     * */


    @PutMapping("/{customerId}/address")
    public ResponseEntity<?> updateAddress(@PathVariable Integer customerId, @RequestParam Integer addressId) {
        try {
            Address address = userService.getAddressById(addressId);
            if (address.getCustomer().getUserId().equals(customerId)) {
                address = userService.setCurrentAddress(customerId, addressId);
                AddressDTO addressDto = AddressMapper.INSTANCE.convertEntityToDto(address);
                return new ResponseEntity<>(addressDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AddressNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> logout(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
}
