package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.model.prescription_medicine.PrescriptionMedicine;
import org.ftf.koifishveterinaryservicecenter.model.prescription_medicine.PrescriptionMedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionMedicineRepository extends JpaRepository<PrescriptionMedicine, PrescriptionMedicineId> {
  }