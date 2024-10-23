package org.ftf.koifishveterinaryservicecenter.controller;


import org.ftf.koifishveterinaryservicecenter.dto.FeedbackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.FeedbackExistedException;
import org.ftf.koifishveterinaryservicecenter.exception.FeedbackNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FeedbackMapper;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.feedbackservice.FeedbackService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final AuthenticationService authenticationService;

    public FeedbackController(
            FeedbackService feedbackService
            , UserService userService
            , AppointmentService appointmentService
            , AuthenticationService authenticationService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/limited")
    public ResponseEntity<?> getFeedbacksForHomePage() {
        List<Feedback> feedbackList = feedbackService.getAllFeedbacks();

        if (feedbackList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            List<FeedbackDto> dtoList = feedbackList.stream().map(FeedbackMapper.INSTANCE::convertToFeedbackDto).toList();
            return ResponseEntity.ok(dtoList);
        }
    }

    /*
     * View all feedback
     * Actors: Manager
     * */
    @GetMapping("/all")
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        if (feedbacks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<FeedbackDto> feedbackDtoList = feedbacks.stream()
                    .map(FeedbackMapper.INSTANCE::convertToFeedbackDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(feedbackDtoList, HttpStatus.OK);
        }
    }

    /*
     * View feedback details
     * Actors: Manager
     * */
    @GetMapping("/{feedbackId}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Integer feedbackId) {
        try {
            Feedback feedback = feedbackService.getFeedbackById(feedbackId);
            FeedbackDto feedbackDto = FeedbackMapper.INSTANCE.feedbackToFeedbackDto(feedback);
            return new ResponseEntity<>(feedbackDto, HttpStatus.OK);
        } catch (FeedbackNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
     * View feedback list of a veterinarian
     * Actors: Veterinarian
     * */
    @GetMapping("/veterinarian")
    public ResponseEntity<?> getFeedbacks() {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByVeterianrianId(authenticationService.getAuthenticatedUserId());
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

    /*
     * View feedback details of a veterinarian
     * Actors: Veterinarian
     * */
    @GetMapping("/{feedbackId}/veterinarian")
    public ResponseEntity<?> getFeedback(@PathVariable("feedbackId") Integer feedbackId) {
        try {
            User veterinarian = userService.getVeterinarianById(authenticationService.getAuthenticatedUserId());
            Feedback feedback = feedbackService.getFeedbackById(feedbackId);
            if (feedback.getVeterinarian().getUserId().equals(veterinarian.getUserId())) {
                FeedbackDto feedbackDto = FeedbackMapper.INSTANCE.feedbackToFeedbackDto(feedback);
                return new ResponseEntity<>(feedbackDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (FeedbackNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Create feedback of an appointment
     * Actors: Customer
     * */
    @PostMapping()
    public ResponseEntity<?> createFeedback(
            @RequestParam Integer appointmentId
            , @RequestBody FeedbackDto feedbackDto) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            if (!appointment.getCustomer().getUserId().equals(authenticationService.getAuthenticatedUserId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Feedback feedback = FeedbackMapper.INSTANCE.convertFeedbackDtoToFeedback(feedbackDto);
            Feedback newFeedback = appointmentService.createFeedback(appointmentId, feedback);
            FeedbackDto newFeedbackDto = FeedbackMapper.INSTANCE.feedbackToFeedbackDto(newFeedback);
            return new ResponseEntity<>(newFeedbackDto, HttpStatus.CREATED);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (FeedbackExistedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * View feedback details of a customer
     * Actors: Customer
     * */
    @GetMapping("/{feedbackId}/customer")
    public ResponseEntity<?> getFeedbackByCustomer(@PathVariable("feedbackId") Integer feedbackId) {
        try {
            User customer = userService.getCustomerById(authenticationService.getAuthenticatedUserId());
            Feedback feedback = feedbackService.getFeedbackById(feedbackId);
            if (feedback.getCustomer().getUserId().equals(customer.getUserId())) {
                FeedbackDto feedbackDto = FeedbackMapper.INSTANCE.convertToFeedbackDto(feedback);
                return new ResponseEntity<>(feedbackDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (FeedbackNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
