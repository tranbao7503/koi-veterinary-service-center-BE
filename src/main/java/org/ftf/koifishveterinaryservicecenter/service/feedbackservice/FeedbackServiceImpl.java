package org.ftf.koifishveterinaryservicecenter.service.feedbackservice;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.FeedbackNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.FeedbackRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserService userService) {
        this.feedbackRepository = feedbackRepository;
        this.userService = userService;
    }

    @Override
    public List<Feedback> getFeedbacksByRating() {
        return feedbackRepository.findFeedbackByRating();
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks;
    }

    @Override
    public List<Feedback> getFeedbacksByVeterianrianId(Integer veterianrianId) throws UserNotFoundException {
        User veterianrian = userService.getVeterinarianById(veterianrianId);
        List<Feedback> feedbacks = feedbackRepository.findByVeterianrianId(veterianrianId);
        if (feedbacks.isEmpty()) {
            throw new FeedbackNotFoundException("Feedback not found with veterianrian id: " + veterianrianId);
        }
        return feedbacks;
    }

    @Override
    public Feedback getFeedbackById(Integer feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(
                () -> new FeedbackNotFoundException("Feedback not found with id: " + feedbackId));
        return feedback;
    }

    @Override
    public Feedback createFeedback(Feedback feedback, Appointment appointment) {
        Feedback newFeedback = new Feedback();

        if (appointment == null) {
            throw new AppointmentNotFoundException("Appointment not found");
        }
        if (appointment.getCustomer() == null) {
            throw new UserNotFoundException("Customer not found");
        }
        if (appointment.getVeterinarian() == null) {
            throw new UserNotFoundException("Veterinarian not found");
        }

        newFeedback.setRating(feedback.getRating());
        newFeedback.setComment(feedback.getComment());
        newFeedback.setDatetime(LocalDateTime.now());
        newFeedback.setAppointment(appointment);
        newFeedback.setCustomer(appointment.getCustomer());
        newFeedback.setVeterinarian(appointment.getVeterinarian());
        newFeedback.setFish(appointment.getFish());

        feedbackRepository.save(newFeedback);

        return newFeedback;
    }

}
