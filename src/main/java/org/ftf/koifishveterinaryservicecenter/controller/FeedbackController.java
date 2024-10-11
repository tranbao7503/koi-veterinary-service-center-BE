package org.ftf.koifishveterinaryservicecenter.controller;


import org.ftf.koifishveterinaryservicecenter.dto.FeedbackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.FeedbackNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FeedbackMapper;
import org.ftf.koifishveterinaryservicecenter.service.feedbackservice.FeedbackService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    public FeedbackController(
            FeedbackService feedbackService
            , UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
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
     * Actors: Manager
     * */
    @GetMapping("/{feedbackId}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Integer feedbackId) {
        try {
            Feedback feedback = feedbackService.getFeedbackById(feedbackId);
            FeedbackDto feedbackDto = FeedbackMapper.INSTANCE.convertToFeedbackDto(feedback);
            return new ResponseEntity<>(feedbackDto, HttpStatus.OK);
        } catch (FeedbackNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
     * Actors: Veterinarian
     * */
    @GetMapping("/veterinarian/{veterinarianId}")
    public ResponseEntity<?> getFeedbacks(@PathVariable("veterinarianId") Integer id) {
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

    /*
     * Actors: Veterinarian
     * */
    @GetMapping("/{feedbackId}/veterinarian/{veterinarianId}")
    public ResponseEntity<?> getFeedback(@PathVariable("feedbackId") Integer feedbackId
            , @PathVariable("veterinarianId") Integer veterinarianId) {
        try {
            User veterinarian = userService.getVeterinarianById(veterinarianId);
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

}
