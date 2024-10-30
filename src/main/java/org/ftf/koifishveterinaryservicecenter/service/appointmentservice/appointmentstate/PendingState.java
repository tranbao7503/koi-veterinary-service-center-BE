package org.ftf.koifishveterinaryservicecenter.service.appointmentservice.appointmentstate;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.enums.SlotStatus;
import org.ftf.koifishveterinaryservicecenter.exception.IllegalStateException;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.service.slotservice.SlotService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PendingState implements AppointmentState {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    private final SlotService slotService;

    public PendingState(AuthenticationService authenticationService, UserService userService, AppointmentRepository appointmentRepository, SlotService slotService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
        this.slotService = slotService;
    }

    @Override
    @Transactional
    public void updateState(Appointment appointment) {
        String roleKey = authenticationService.getAuthenticatedUserRoleKey();

        if (!roleKey.equals("STA"))
            throw new IllegalStateException("Only Staff can update appointments from PENDING to CONFIRMED");

        // set new status for the appointment
        appointment.setCurrentStatus(AppointmentStatus.CONFIRMED);

        // Update Veterinarian_Slot status
        TimeSlot timeSlot = slotService.getTimeSlotById(appointment.getTimeSlot().getSlotId());
        slotService.updateVeterinarianSlotsStatus(appointment.getVeterinarian().getUserId(), timeSlot.getSlotId(), SlotStatus.BOOKED);

        // get staff Id from authenticated User in order to log
        Integer staffId = authenticationService.getAuthenticatedUserId();
        User staff = userService.getUserProfile(staffId);

        // insert to Status table
        logToStatus(appointment, staff);
        appointmentRepository.save(appointment);
    }

    private void logToStatus(Appointment appointment, User staff) {
        Status status = new Status();
        status.setAppointment(appointment);
        status.setStatusName(appointment.getCurrentStatus());
        status.setTime(LocalDateTime.now());

        status.setNote("Staff - " + staff.getFirstName() + " " + staff.getLastName() + " update CONFIRMED successfully");
        appointment.addStatus(status);
    }
}
