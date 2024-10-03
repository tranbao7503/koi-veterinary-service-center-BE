package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine.PrescriptionMedicine;
import org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine.PrescriptionMedicineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrescriptionMedicineRepository extends JpaRepository<PrescriptionMedicine, PrescriptionMedicineId> {

}