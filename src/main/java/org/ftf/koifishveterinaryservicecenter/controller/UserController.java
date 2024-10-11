package org.ftf.koifishveterinaryservicecenter.controller;


import lombok.extern.slf4j.Slf4j;
import org.ftf.koifishveterinaryservicecenter.dto.*;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentForListDto;
import org.ftf.koifishveterinaryservicecenter.dto.response.AuthenticationResponse;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.*;
import org.ftf.koifishveterinaryservicecenter.exception.*;
import org.ftf.koifishveterinaryservicecenter.mapper.*;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.feedbackservice.FeedbackService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;
    private final FeedbackService feedbackService;
    private final AppointmentService appointmentService;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, AuthenticationService authenticationService, FeedbackService feedbackService, AppointmentService appointmentService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
        this.feedbackService = feedbackService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam Integer userId) {

        Integer userIdFromToken = userId;  // the userId takes from Authentication object in SecurityContext
        User user = userService.getUserProfile(userId);
        UserDTO userDto = UserMapper.INSTANCE.convertEntityToDto(user);
        return ResponseEntity.ok(userDto);
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
            List<UserDTO> userDTOs = customers.stream()
                    .map(userMapper::convertEntityToDto)
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


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTOFromRequest) {
        try {
            String username = userDTOFromRequest.getUsername();
            String password = userDTOFromRequest.getPassword();
            String firstName = userDTOFromRequest.getFirstName();
            String lastName = userDTOFromRequest.getLastName();

            userService.signUp(username, password, firstName, lastName);
            return new ResponseEntity<>("Sign up successfully", HttpStatus.OK);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/veterinarian/{veterinarianId}/feedbacks/{feedbackId}")
    public ResponseEntity<?> getFeedback(@PathVariable("feedbackId") Integer feedbackId
            , @PathVariable("veterinarianId") Integer veterinarianId) {
        try {
            Feedback feedback = feedbackService.getFeedbackById(feedbackId);
            if (feedback.getVeterinarian().getUserId().equals(veterinarianId)) {
                FeedbackDto feedbackDto = FeedbackMapper.INSTANCE.feedbackToFeedbackDto(feedback);
                return new ResponseEntity<>(feedbackDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (FeedbackNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }
    /*
     * Update avatar of user
     * Actors: Customer, Manager
     * */

    @PreAuthorize("hasAuthority('CUS')")
    @PutMapping("/avatar")
    public ResponseEntity<?> updateUserAvatar(@RequestParam("user_id") Integer userId
            , @RequestParam("image") MultipartFile image) {
        try {
            User user = userService.updateUserAvatar(userId, image);
            UserDTO userDto = UserMapper.INSTANCE.convertEntityToDtoIgnoreAddress(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/veterinarians/{veterinarianId}/certificate")
    public ResponseEntity<?> addVeterinarianCertificate(
            @PathVariable("veterinarianId") Integer veterinarianId
            , @RequestParam("certificateName") String certificateName
            , @RequestParam("file") MultipartFile file) {
        try {
            String path = userService.AddVeterinarianCertificate(veterinarianId, certificateName, file);
            return new ResponseEntity<>(path, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/veterinarians")
    public ResponseEntity<?> getAllVeterinarians() {
        try {
            List<User> veterinarians = userService.getAllVeterinarians();
            List<UserDTO> userDTOs = veterinarians.stream()
                    .map(UserMapper.INSTANCE::convertEntityToDtoIgnoreAddress)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/veterinarian/{id}/feedbacks")
    public ResponseEntity<?> getFeedbacks(@PathVariable("id") Integer id) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByVeterianrianId(id);
            List<FeedbackDto> feedbackDtos = feedbacks.stream()
                    .map(feedback -> FeedbackMapper.INSTANCE.convertToFeedbackDto(feedback))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(feedbackDtos, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (FeedbackNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/veterinarians/{veterinarianId}/certificate")
    public ResponseEntity<?> getCertificate(@PathVariable("veterinarianId") Integer veterinarianId) {
        try {
            List<Certificate> certificates = userService.getAllCertificatesByVeterinarianId(veterinarianId);
            List<CertificateDto> certificateDtos = certificates.stream()
                    .map(CertificateMapper.INSTANCE::convertToCertificateDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(certificateDtos, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customers/{customerId}/appointments")
    public ResponseEntity<?> getAppointments(@PathVariable("customerId") Integer customerId) {
        try{
            User customer = userService.getCustomerById(customerId); // Check whether customer existed
            List<Appointment> appointments = appointmentService.getAppointmentsByCustomerId(customer.getUserId());
            List<AppointmentForListDto> appointmentForListDtos = appointments.stream()
                    .map(AppointmentMapper.INSTANCE::convertedToAppointmentDtoForList)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(appointmentForListDtos, HttpStatus.OK);
        } catch (AppointmentServiceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
