package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentMethod;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.IllegalStateException;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckInState implements AppointmentState {


    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final AppointmentRepository appointmentRepository;

    public CheckInState(UserService userService, AuthenticationService authenticationService, AppointmentRepository appointmentRepository) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void updateState(Appointment appointment) {

        String roleKey = authenticationService.getAuthenticatedUserRoleKey();

        Payment payment = appointment.getPayment();

        if (payment.getStatus().equals(PaymentStatus.NOT_PAID)) {
            throw new IllegalStateException("Appointment cannot update to DONE due to Payment is not paid yet");
        }

        if (roleKey.equals("STA") || roleKey.equals("VET")) {
            // set new status for appointment
            appointment.setCurrentStatus(AppointmentStatus.DONE);

            // get actor Id from authenticated User in order to log
            Integer userId = authenticationService.getAuthenticatedUserId();
            User actor = userService.getUserProfile(userId);

            // insert into Status table
            logToStatus(appointment, actor);
            appointmentRepository.save(appointment);
        }
        throw new IllegalStateException("Only Staff/Veterinarian can update appointments from CHECKIN to DONE");

    }

    private void logToStatus(Appointment appointment, User confirmedActor) {
        Status status = new Status();
        String actor = "";

        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus());
        status.setTime(LocalDateTime.now());


        PaymentMethod paymentMethod = appointment.getPayment().getPaymentMethod();
        String roleKey = confirmedActor.getRole().getRoleKey();

        // set status
        if (paymentMethod.equals(PaymentMethod.VN_PAY) && roleKey.equals("STA")) {
            status.setNote(produceLogMessage(confirmedActor, roleKey));
        }
        if (paymentMethod.equals(PaymentMethod.CASH) && ((roleKey.equals("STA") || roleKey.equals("VET")))) {
            status.setNote(produceLogMessage(confirmedActor, roleKey));
        }
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

}
