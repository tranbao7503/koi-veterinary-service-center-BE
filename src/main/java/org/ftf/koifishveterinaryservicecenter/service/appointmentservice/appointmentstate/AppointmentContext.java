package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;

public class AppointmentContext {
    private final AppointmentState appointmentState;

    public AppointmentContext(Appointment updatedAppointment, AppointmentStateFactory appointmentStateFactory) {
        this.appointmentState = appointmentStateFactory.getState(updatedAppointment.getCurrentStatus());
    }

    public void update(Appointment appointment) {
        appointmentState.updateState(appointment);
    }


}
