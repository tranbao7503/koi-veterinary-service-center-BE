package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmedState implements AppointmentState {


    private final AppointmentRepository appointmentRepository;

    public ConfirmedState(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
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
