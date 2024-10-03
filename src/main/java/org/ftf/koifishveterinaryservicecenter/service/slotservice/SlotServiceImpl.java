package org.ftf.koifishveterinaryservicecenter.service.slotservice;

import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlots;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.VetrinarianSlotsNotFound;
import org.ftf.koifishveterinaryservicecenter.repository.TimeSlotRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.ftf.koifishveterinaryservicecenter.repository.VeterinarianSlotsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SlotServiceImpl implements SlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final VeterinarianSlotsRepository veterinarianSlotsRepository;
    private final UserRepository userRepository;

    @Autowired
    public SlotServiceImpl(TimeSlotRepository timeSlotRepository, VeterinarianSlotsRepository veterinarianSlotsRepository, UserRepository userRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.veterinarianSlotsRepository = veterinarianSlotsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<VeterinarianSlots> getVeterinarianSlots(Integer veterinarianId) {
        User veterinarian = userRepository.findVeterinarianById(veterinarianId);
        if(veterinarian == null) { // Veterinarian not found
            throw new UserNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        } else { // Veterinarian existed
            List<VeterinarianSlots> veterinarianSlotsList = veterinarianSlotsRepository.findByVeterinarianId(veterinarianId);
            if(veterinarianSlotsList.isEmpty()) { // Veterianrian has no slots
                throw new VetrinarianSlotsNotFound("Veterinarian with ID: " + veterinarianId + " has no shift schedule yet");
            } else { // Veterinarian having slots
                return veterinarianSlotsList;
            }
        }

    }
}
