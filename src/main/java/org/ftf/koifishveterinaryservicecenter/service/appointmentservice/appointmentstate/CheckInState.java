package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.IllegalStateException;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.service.statusservice.StatusService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckInState implements AppointmentState {


    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final AppointmentRepository appointmentRepository;
    private final StatusService statusService;

    public CheckInState(UserService userService, AuthenticationService authenticationService, AppointmentRepository appointmentRepository, StatusService statusService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.appointmentRepository = appointmentRepository;
        this.statusService = statusService;
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

        // veterinarian logs done
        statusService.veterinarianLogDoneAppointment(appointment, actor);
        appointmentRepository.save(appointment);

    }
}
