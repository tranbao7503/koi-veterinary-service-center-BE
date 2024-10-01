package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.FeedbackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.mapper.FeedbackMapper;
import org.ftf.koifishveterinaryservicecenter.service.feedback.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        if (feedbacks.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            List<FeedbackDto> feedbackDtoList = feedbacks.stream()
                    .map(FeedbackMapper.INSTANCE::convertToFeedbackDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(feedbackDtoList, HttpStatus.OK);
        }
    }
}
