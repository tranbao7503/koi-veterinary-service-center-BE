package org.ftf.koifishveterinaryservicecenter.controller;


import org.ftf.koifishveterinaryservicecenter.dto.FeedbackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.exception.FeedbackNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.FeedbackMapper;
import org.ftf.koifishveterinaryservicecenter.service.feedbackservice.FeedbackService;
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

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
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

}
