package org.ftf.koifishveterinaryservicecenter.service.feedbackservice;

import org.ftf.koifishveterinaryservicecenter.dto.FeedBackDto;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.mapper.FeedbackMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.ServiceMapper;
import org.ftf.koifishveterinaryservicecenter.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findFeedbackByRating();
    }

}
