package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
  @Query("select s from Feedback s where s.veterinarian.userId = :veterinarianId")
  List<Feedback> findByVeterianrianId(@Param("veterinarianId") Integer veterianrianId);
  }