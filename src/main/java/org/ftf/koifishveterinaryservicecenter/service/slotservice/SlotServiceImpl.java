package org.ftf.koifishveterinaryservicecenter.service.slotservice;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.TimeSlotNotFound;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
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

    @Override   // get all available slots prior to current_time 3 hours
    public List<TimeSlot> getAvailableSlots() {

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime threeHoursFromNow = currentDate.plusHours(3);

        Integer nextThreeHour = threeHoursFromNow.getHour();

        Integer nextSlot = 1;
        if (nextThreeHour <= 10) {
            nextSlot = 2;
        } else if (nextThreeHour <= 13) {
            nextSlot = 3;
        } else if (nextThreeHour <= 15) {
            nextSlot = 4;
        } else {
            currentDate = currentDate.plusDays(1);
        }

        Integer currentYear = currentDate.getYear();
        Integer currentMonth = currentDate.getMonthValue();
        Integer currentDay = currentDate.getDayOfMonth();

        LocalDateTime endDate = currentDate.plusDays(30);
        Integer endYear = endDate.getYear();
        Integer endMonth = endDate.getMonthValue();
        Integer endDay = endDate.getDayOfMonth();

        List<TimeSlot> timeSlots = timeSlotRepository.findAvailableTimeSlot(currentYear, currentMonth, currentDay, nextSlot, endYear, endMonth, endDay);
        if (timeSlots.isEmpty()) {
            throw new TimeSlotNotFound("There are no available slots");
        }
        return timeSlots;
    }

    @Override
    public List<TimeSlot> getAvailableSlotsByVeterinarianId(Integer veterinarianId) throws UserNotFoundException {
        User veterinarian = userService.getVeterinarianById(veterinarianId);

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime threeHoursFromNow = currentDate.plusHours(3);

        Integer nextThreeHour = threeHoursFromNow.getHour();

        Integer beginSlot = 1;
        if (nextThreeHour <= 10) {
            beginSlot = 2;
        } else if (nextThreeHour <= 13) {
            beginSlot = 3;
        } else if (nextThreeHour <= 15) {
            beginSlot = 4;
        } else {
            currentDate = currentDate.plusDays(1);
        }

        Integer currentYear = currentDate.getYear();
        Integer currentMonth = currentDate.getMonthValue();
        Integer currentDay = currentDate.getDayOfMonth();

        LocalDateTime endDate = currentDate.plusDays(30);
        Integer endYear = endDate.getYear();
        Integer endMonth = endDate.getMonthValue();
        Integer endDay = endDate.getDayOfMonth();

        List<TimeSlot> timeSlots = timeSlotRepository.findAvailableTimeSlotByVeterinarianId(veterinarian.getUserId(), currentYear, currentMonth, currentDay, beginSlot, endYear, endMonth, endDay);
        if (timeSlots.isEmpty()) {
            throw new TimeSlotNotFound("There are no available slots");
        }
        return timeSlots;
    }


}
