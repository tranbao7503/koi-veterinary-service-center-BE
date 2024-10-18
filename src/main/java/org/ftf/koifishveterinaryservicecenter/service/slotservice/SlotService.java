package org.ftf.koifishveterinaryservicecenter.service.slotservice;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlots;

import java.util.List;

public interface SlotService {

    List<TimeSlot> getVeterinarianSlots(Integer veterinarianId);
    TimeSlot getTimeSlotById(Integer timeSlotId);
    List<TimeSlot> getListAvailableTimeSlots();
    List<VeterinarianSlots> getVeterinarianSlotsBySlotId(Integer slotId);
    List<TimeSlot> getAvailableSlotsByVeterinarianId(Integer veterinarianId);

}
