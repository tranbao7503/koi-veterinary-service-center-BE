package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.IllegalStateException;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckInState implements AppointmentState {


    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final AppointmentRepository appointmentRepository;

    public CheckInState(UserService userService, AuthenticationService authenticationService, AppointmentRepository appointmentRepository) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void updateState(Appointment appointment) {

        String roleKey = authenticationService.getAuthenticatedUserRoleKey();

        Payment payment = appointment.getPayment();

        if (payment.getStatus().equals(PaymentStatus.NOT_PAID)) {
            throw new IllegalStateException("Appointment cannot update to DONE due to Payment is not paid yet");
        }

        if (!roleKey.equals("VET")) {
            throw new IllegalStateException("Only Veterinarian can update appointments from CHECKIN to DONE");
        }

        // set new status for appointment
        appointment.setCurrentStatus(AppointmentStatus.DONE);

        // get actor Id from authenticated User in order to log
        Integer userId = authenticationService.getAuthenticatedUserId();
        User actor = userService.getUserProfile(userId);

        // insert into Status table
        logToStatus(appointment, actor);
        appointmentRepository.save(appointment);

    }

    private void logToStatus(Appointment appointment, User veterinarian) {
        Status status = new Status();
        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus().toString());
        status.setTime(LocalDateTime.now());
        status.setNote("Veterinarian - " + veterinarian.getFirstName() + " " + veterinarian.getLastName() + " marked DONE the appointment successfully");
        status.setUser(veterinarian);
        appointment.addStatus(status);
    }

}
