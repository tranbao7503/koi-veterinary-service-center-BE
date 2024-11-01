package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentUpdatedException;

public class AppointmentContext {
    private final AppointmentState appointmentState;

    public AppointmentContext(Appointment updatedAppointment, AppointmentStateFactory appointmentStateFactory) {
        this.appointmentState = appointmentStateFactory.getState(updatedAppointment.getCurrentStatus());
    }

    public void update(Appointment appointment) throws AppointmentUpdatedException {
        appointmentState.updateState(appointment);
    }


}
