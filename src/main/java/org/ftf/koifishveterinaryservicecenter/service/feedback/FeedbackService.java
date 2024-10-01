package org.ftf.koifishveterinaryservicecenter.service.feedback;

import org.ftf.koifishveterinaryservicecenter.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAllFeedbacks();
    List<Feedback> getFeedbacksByVeterianrianId(Integer veterianrianId);
    Feedback getFeedbackById(Integer feedbackId);
}
