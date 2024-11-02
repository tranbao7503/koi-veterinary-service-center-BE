package org.ftf.koifishveterinaryservicecenter.service.statusservice;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;

public interface StatusService {

    void staffLogConfirmedAppointment(Appointment appointment, User staff);

    void systemLogOngoingAppointment(Appointment appointment);

    void actorLogCheckInAppointment(Appointment appointment, User checkInActor);

    void veterinarianLogDoneAppointment(Appointment appointment, User veterinarian);

    void customerLogStatusPayment(Appointment appointment, User customer, PaymentStatus paymentStatus);

}
