package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishRepository extends JpaRepository<Fish, Integer> {

    List<Fish> findAllFishByCustomer_UserId(int userId);

     Fish findByFishId(int fishId);
  }