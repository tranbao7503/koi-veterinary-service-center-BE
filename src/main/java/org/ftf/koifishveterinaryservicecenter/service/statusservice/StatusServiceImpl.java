package org.ftf.koifishveterinaryservicecenter.service.statusservice;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentMethod;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void staffLogConfirmedAppointment(Appointment appointment, User staff) {
        Status status = new Status();
        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus().toString());
        status.setTime(LocalDateTime.now());

        status.setNote("Staff - " + staff.getFirstName() + " " + staff.getLastName() + " update CONFIRMED successfully");
        status.setUser(staff);
        appointment.addStatus(status);
    }

    @Override
    public void systemLogOngoingAppointment(Appointment appointment) {
        Status status = new Status();
        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus().toString());
        status.setTime(LocalDateTime.now());
        status.setNote("System - marked ON_GOING the appointment successfully");
        appointment.addStatus(status);
    }

    @Override
    public void actorLogCheckInAppointment(Appointment appointment, User checkInActor) {
        Status status = new Status();

        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus().toString());
        status.setTime(LocalDateTime.now());


        PaymentMethod paymentMethod = appointment.getPayment().getPaymentMethod();
        String roleKey = checkInActor.getRole().getRoleKey();

        // set status
        if (paymentMethod.equals(PaymentMethod.VN_PAY) && roleKey.equals("STA")) {
            status.setNote(produceLogMessage(checkInActor, roleKey));
        }

        if (paymentMethod.equals(PaymentMethod.CASH) && ((roleKey.equals("STA") || roleKey.equals("VET")))) {
            status.setNote(produceLogMessage(checkInActor, roleKey));
        }

        status.setUser(checkInActor);
        appointment.addStatus(status);
    }

    private String produceLogMessage(User confirmedActor, String roleKey) {
        String logMessage = "";

        if (roleKey.equals("VET"))
            logMessage = "Veterinarian - " + confirmedActor.getFirstName() + " " + confirmedActor.getLastName() + " update CHECK-IN successfully";
        if (roleKey.equals("STA"))
            logMessage = "Staff - " + confirmedActor.getFirstName() + " " + confirmedActor.getLastName() + " update CHECK-IN successfully";
        return logMessage;
    }

    @Override
    public void veterinarianLogDoneAppointment(Appointment appointment, User veterinarian) {
        Status status = new Status();
        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus().toString());
        status.setTime(LocalDateTime.now());
        status.setNote("Veterinarian - " + veterinarian.getFirstName() + " " + veterinarian.getLastName() + " marked DONE the appointment successfully");
        status.setUser(veterinarian);
        appointment.addStatus(status);
    }

    @Override
    public void customerLogStatusPayment(Appointment appointment, User customer, PaymentStatus paymentStatus) {
        Status status = new Status();
        status.setStatusName(String.valueOf(paymentStatus));
        status.setTime(LocalDateTime.now());
        status.setNote("Customer - " + customer.getFirstName() + " " + customer.getLastName() + " " + paymentStatus + " successfully");
        status.setAppointment(appointment);
        status.setUser(customer);
        statusRepository.save(status);
    }
}
