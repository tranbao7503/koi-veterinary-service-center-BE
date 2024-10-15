package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FishRepository extends JpaRepository<Fish, Integer> {

    List<Fish> findAllFishByCustomer_UserId(int userId);

    Fish findByFishId(int fishId);
  Optional<Fish> findByFishId(Integer fishId);
  }