package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.IllegalStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * This class is used to produce implementation of AppointmentState based on AppointmentStatus
 */

@Component
public class AppointmentStateFactory {
    private final PendingState pendingState;
    private final ConfirmedState confirmedState;
    private final CheckInState checkInState;
    private final OngoingState ongoingState;

    @Autowired
    public AppointmentStateFactory(PendingState pendingState, ConfirmedState confirmedState, CheckInState checkInState, OngoingState ongoingState) {
        this.pendingState = pendingState;
        this.confirmedState = confirmedState;
        this.checkInState = checkInState;
        this.ongoingState = ongoingState;
    }

    public AppointmentState getState(AppointmentStatus appointmentStatus) {
        if (appointmentStatus == AppointmentStatus.PENDING) return pendingState;
        if (appointmentStatus == AppointmentStatus.CONFIRMED) return confirmedState;
        if (appointmentStatus == AppointmentStatus.CHECKED_IN) return checkInState;
        if (appointmentStatus == AppointmentStatus.ON_GOING) return ongoingState;
        throw new IllegalStateException("Invalid supported appointment status: " + appointmentStatus);
    }
}
