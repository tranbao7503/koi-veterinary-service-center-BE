package org.ftf.koifishveterinaryservicecenter.service.slotservice;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.TimeSlotNotFound;
import org.ftf.koifishveterinaryservicecenter.repository.TimeSlotRepository;
import org.ftf.koifishveterinaryservicecenter.repository.VeterinarianSlotsRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SlotServiceImpl implements SlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final VeterinarianSlotsRepository veterinarianSlotsRepository;
    private final UserService userService;

    @Autowired
    public SlotServiceImpl(TimeSlotRepository timeSlotRepository, VeterinarianSlotsRepository veterinarianSlotsRepository, UserService userService) {
        this.timeSlotRepository = timeSlotRepository;
        this.veterinarianSlotsRepository = veterinarianSlotsRepository;
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
    public List<TimeSlot> getAvailableSlots() {

        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(30);

        Integer currentYear = currentDate.getYear();
        Integer currentMonth = currentDate.getMonthValue();
        Integer currentDay = currentDate.getDayOfMonth();

        Integer endYear = endDate.getYear();
        Integer endMonth = endDate.getMonthValue();
        Integer endDay = endDate.getDayOfMonth();

        List<TimeSlot> timeSlots = timeSlotRepository.findAvailableTimeSlot(currentYear, currentMonth, currentDay, endYear, endMonth, endDay);
        if (timeSlots.isEmpty()) {
            throw new TimeSlotNotFound("There are no available slots");
        }
        return timeSlots;
    }


}
