package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.FeedBackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.mapper.FeedbackMapper;
import org.ftf.koifishveterinaryservicecenter.service.feedbackservice.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;



    @Autowired
    public FeedbackController(FeedbackService feedbackService, FeedbackMapper feedbackMapper) {
        this.feedbackService = feedbackService;
        this.feedbackMapper = feedbackMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAllFeedbacks() {
        List<Feedback> feedbackList = feedbackService.getAllFeedbacks();

        if(feedbackList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else {
            List<FeedBackDto> dtoList = feedbackMapper.convertEntitiesToDtos(feedbackList);
            return ResponseEntity.ok(dtoList);
        }
    }
}
