package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentUpdatedException;

public interface AppointmentState {
    void updateState(Appointment appointment) throws AppointmentUpdatedException;
}
