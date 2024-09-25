package org.ftf.koifishveterinaryservicecenter.koifishservice.repository;

import org.ftf.koifishveterinaryservicecenter.koifishservice.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
  }