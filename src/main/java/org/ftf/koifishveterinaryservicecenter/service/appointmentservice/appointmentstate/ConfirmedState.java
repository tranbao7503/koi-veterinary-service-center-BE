package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmedState implements AppointmentState {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;

    public ConfirmedState(AuthenticationService authenticationService, UserService userService, AppointmentRepository appointmentRepository) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Async
    public void updateState(Appointment appointment) {

        // set a new status for the appointment
        appointment.setCurrentStatus(AppointmentStatus.ON_GOING);

        // logging to Status code
        logToStatus(appointment);
        appointmentRepository.save(appointment);
    }

    private void logToStatus(Appointment appointment) {
        Status status = new Status();
        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus());
        status.setTime(LocalDateTime.now());
        status.setNote("System - marked ON_GOING the appointment successfully");
        appointment.addStatus(status);
    }
}
