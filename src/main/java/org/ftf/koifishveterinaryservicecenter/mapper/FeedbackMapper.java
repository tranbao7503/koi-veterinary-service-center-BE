package org.ftf.koifishveterinaryservicecenter.mapper;


import org.ftf.koifishveterinaryservicecenter.dto.FeedBackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackMapper {

    public FeedbackMapper() {
    }

    // this convert method is only used to render a special FeedbackDto on homepage
    public FeedBackDto convertEntityToDto(Feedback feedback) {
        FeedBackDto feedBackDto = new FeedBackDto();
        feedBackDto.setFeedbackId(feedback.getFeedbackId());
        feedBackDto.setRating(feedback.getRating());
        feedBackDto.setComment(feedback.getComment());
        return feedBackDto;
    }

    public List<FeedBackDto> convertEntitiesToDtos(List<Feedback> feedbacks) {
        List<FeedBackDto> dtoList = feedbacks.stream().map(this::convertEntityToDto).toList();
        return dtoList;
    }
}
