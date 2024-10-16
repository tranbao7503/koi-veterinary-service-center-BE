package org.ftf.koifishveterinaryservicecenter.service.feedbackservice;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.ftf.koifishveterinaryservicecenter.entity.User;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getFeedbacksByRating();

    List<Feedback> getAllFeedbacks();

    List<Feedback> getFeedbacksByVeterianrianId(Integer veterianrianId);

    Feedback getFeedbackById(Integer feedbackId);

    Feedback createFeedback(Feedback feedback, Appointment appointment);
}
