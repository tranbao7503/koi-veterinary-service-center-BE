package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MedicalReportRepository extends JpaRepository<MedicalReport, Integer> {
  MedicalReport findByReportId(Integer medicalReportId);
}