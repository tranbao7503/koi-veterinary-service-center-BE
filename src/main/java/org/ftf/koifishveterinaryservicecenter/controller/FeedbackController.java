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

@RestController
//@CrossOrigin
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;

    public FeedbackController(FeedbackService feedbackService, FeedbackMapper feedbackMapper) {
        this.feedbackService = feedbackService;
        this.feedbackMapper = feedbackMapper;
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        if (feedbacks.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            List<FeedbackDto> feedbackDtoList = feedbackMapper.convertToDtoList(feedbacks);
            return new ResponseEntity<>(feedbackDtoList, HttpStatus.OK);
        }
    }
}
