package org.ftf.koifishveterinaryservicecenter.service.slotservice;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.TimeSlotNotFound;
import org.ftf.koifishveterinaryservicecenter.repository.TimeSlotRepository;
import org.ftf.koifishveterinaryservicecenter.repository.VeterinarianSlotsRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SlotServiceImpl implements SlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final UserService userService;

    @Autowired
    public SlotServiceImpl(TimeSlotRepository timeSlotRepository, UserService userService) {
        this.timeSlotRepository = timeSlotRepository;
        this.userService = userService;
    }

    @Override
    public List<TimeSlot> getVeterinarianSlots(Integer veterinarianId) {
        User veterinarian = userService.getVeterinarianById(veterinarianId);

        // Veterinarian existed
        List<TimeSlot> veterinarianSlotsList = timeSlotRepository.findByVeterinarianId(veterinarianId);
        if (veterinarianSlotsList.isEmpty()) { // Veterianrian has no slots
            throw new TimeSlotNotFound("Veterinarian with ID: " + veterinarianId + " has no shift schedule yet");
        } // Veterinarian having slots

        // Filter appointments to retain only the one with matching veterinarianId
        for (TimeSlot timeSlot : veterinarianSlotsList) {
            timeSlot.setAppointments(timeSlot.getAppointments().stream().filter(appointment -> appointment.getVeterinarian().getUserId().equals(veterinarianId)).limit(1)  // Keep only one appointment with the matching veterinarianId
                    .collect(Collectors.toSet()));
        }
        return veterinarianSlotsList;
    }

    @Override
    public TimeSlot getTimeSlotById(Integer timeSlotId) {
        Optional<TimeSlot> timeSlot = timeSlotRepository.findById(timeSlotId);
        if (timeSlot.isEmpty()) throw new TimeSlotNotFound("Time slot with ID: " + timeSlotId + " not found");
        return timeSlot.get();
    }

    @Override
    public List<TimeSlot> getListAvailableTimeSlots() {

        LocalDateTime threeHoursFromNow = LocalDateTime.now().plusHours(3);
        LocalDateTime threeMonthsFromNow = LocalDateTime.now().plusMonths(3);

        List<TimeSlot> availableTimeSlot = timeSlotRepository.getAvailableTimeSlot();
        return availableTimeSlot.stream().filter(timeSlot -> timeSlot.getDateTimeBasedOnSlot().isAfter(threeHoursFromNow) && timeSlot.getDateTimeBasedOnSlot().isBefore(threeMonthsFromNow)).toList();
    }


}
