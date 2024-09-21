package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.model.veterinarian_slots.VeterinarianSlotId;
import org.ftf.koifishveterinaryservicecenter.model.veterinarian_slots.VeterinarianSlots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarianSlotsRepository extends JpaRepository<VeterinarianSlots, VeterinarianSlotId> {
  }