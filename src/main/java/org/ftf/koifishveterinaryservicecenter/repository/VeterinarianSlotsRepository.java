package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlotId;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlots;
import org.ftf.koifishveterinaryservicecenter.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VeterinarianSlotsRepository extends JpaRepository<VeterinarianSlots, VeterinarianSlotId> {
    @Query("SELECT vs FROM VeterinarianSlots vs WHERE vs.veterinarian.userId = :veterinarianId")
    List<VeterinarianSlots> findByVeterinarianId(@Param("veterinarianId") Integer veterinarianId);

    @Query("SELECT vs FROM VeterinarianSlots vs WHERE vs.status = ?1 AND vs.veterinarianSlotId.slotId = ?2")
    List<VeterinarianSlots> getAvailableSlotsByVeterinarianId(SlotStatus status, Integer slotId);
  }