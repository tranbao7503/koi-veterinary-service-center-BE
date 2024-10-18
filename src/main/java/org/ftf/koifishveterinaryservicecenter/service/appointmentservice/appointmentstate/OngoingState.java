package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.stereotype.Service;

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

    }
}