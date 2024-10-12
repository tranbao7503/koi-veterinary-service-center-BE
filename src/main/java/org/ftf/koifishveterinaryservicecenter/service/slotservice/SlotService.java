package org.ftf.koifishveterinaryservicecenter.service.slotservice;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;

import java.util.List;

public interface SlotService {

    List<TimeSlot> getVeterinarianSlots(Integer veterinarianId);

    TimeSlot getTimeSlotById(Integer timeSlotId);

    List<TimeSlot> getAvailableSlots();

}
