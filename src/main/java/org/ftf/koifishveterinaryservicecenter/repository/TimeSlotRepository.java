package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
  @Query("SELECT t FROM TimeSlot t JOIN t.veterinarians v WHERE v.userId = :veterinarianId")
  List<TimeSlot> findByVeterinarianId(Integer veterinarianId);
  }