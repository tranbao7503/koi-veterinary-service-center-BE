package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
    @Query("SELECT t FROM TimeSlot t JOIN VeterinarianSlots vs ON t.slotId = vs.veterinarianSlotId.slotId WHERE vs.veterinarian.userId = :veterinarianId AND vs.status = 'BOOKED'")
    List<TimeSlot> findByVeterinarianId(Integer veterinarianId);
  }