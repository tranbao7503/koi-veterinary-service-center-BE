package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.IllegalStateException;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OngoingState implements AppointmentState {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;

    public OngoingState(AuthenticationService authenticationService, UserService userService, AppointmentRepository appointmentRepository) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void updateState(Appointment appointment) {
        String roleKey = authenticationService.getAuthenticatedUserRoleKey();

        if (roleKey.equals("VET")) {
            appointment.setCurrentStatus(AppointmentStatus.CHECKED_IN);

            // get vetId from the appointment
            Integer veterinarianId = appointment.getVeterinarian().getUserId();
            User veterinarian = userService.getVeterinarianById(veterinarianId);

            // logging to Status table
            logToStatus(appointment, veterinarian);
            appointmentRepository.save(appointment);
        } else throw new IllegalStateException("Only Veterinarian can update appointments from ON_GOING to CHECKED_IN");
    }

    private void logToStatus(Appointment appointment, User veterinarian) {
        Status status = new Status();
        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus());
        status.setTime(LocalDateTime.now());
        status.setNote("Veterinarian - " + veterinarian.getFirstName() + " " + veterinarian.getLastName() + " marked DONE the appointment successfully");
        appointment.addStatus(status);
    }
}