package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.service.statusservice.StatusService;
import org.springframework.stereotype.Service;

@Service
public class ConfirmedState implements AppointmentState {


    private final AppointmentRepository appointmentRepository;
    private final StatusService statusService;

    public ConfirmedState(AppointmentRepository appointmentRepository, StatusService statusService) {
        this.appointmentRepository = appointmentRepository;
        this.statusService = statusService;
    }

    @Override
    public void updateState(Appointment appointment) {

        // set a new status for the appointment
        appointment.setCurrentStatus(AppointmentStatus.ON_GOING);

        // system logs ongoing
        statusService.systemLogOngoingAppointment(appointment);
        appointmentRepository.save(appointment);
    }
}
