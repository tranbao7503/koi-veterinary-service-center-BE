package org.ftf.koifishveterinaryservicecenter.service.slotservice;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlots;

import java.util.List;

public interface SlotService {
    List<VeterinarianSlots> getVeterinarianSlots(Integer veterinarianId);
}
