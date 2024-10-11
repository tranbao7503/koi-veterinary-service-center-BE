package org.ftf.koifishveterinaryservicecenter.service.medicalreportservice;

import org.ftf.koifishveterinaryservicecenter.dto.PrescriptionDto;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Medicine;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;

import java.util.List;

public interface MedicalReportService {
    List<Medicine> getAllMedicines();

    PrescriptionDto createPrescription(PrescriptionDto prescriptionDto);

    Prescription findPrescriptionById(Integer prescriptionId);
}
