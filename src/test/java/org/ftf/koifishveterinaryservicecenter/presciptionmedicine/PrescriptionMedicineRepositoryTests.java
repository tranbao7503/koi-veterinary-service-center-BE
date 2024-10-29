package org.ftf.koifishveterinaryservicecenter.presciptionmedicine;

import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Medicine;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine.PrescriptionMedicine;
import org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine.PrescriptionMedicineId;
import org.ftf.koifishveterinaryservicecenter.repository.PrescriptionMedicineRepository;
import org.ftf.koifishveterinaryservicecenter.repository.PrescriptionRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class PrescriptionMedicineRepositoryTests {

    @Autowired
    private PrescriptionMedicineRepository prescriptionMedicineRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateMedicalReportSuccess() {
        Medicine medicine1 = new Medicine();
        medicine1.setMedicineId(20);
        medicine1.setMedicineName("Probiotic B");

        Medicine medicine2 = new Medicine();
        medicine2.setMedicineId(19);
        medicine2.setMedicineName("Anti-stress Formula");

        Set<Medicine> medicines = Set.of(medicine1, medicine2);

        // create a new Prescription instance
        Prescription prescription = new Prescription();
        prescription.setInstruction("Use antibiotic cream twice daily.");
        Prescription savedPrescription = prescriptionRepository.save(prescription);   // in order to generate prescription_id

        Set<PrescriptionMedicine> prescriptionMedicines = new HashSet<>();
        medicines.forEach(medicine -> {
            // set props for new PrescriptionMedicine
            PrescriptionMedicineId pmId = new PrescriptionMedicineId(savedPrescription.getPrescriptionId(), medicine.getMedicineId());
            PrescriptionMedicine pm = new PrescriptionMedicine(pmId, 10, "");
            pm.setPrescription(savedPrescription);
            pm.setMedicine(medicine);
            prescriptionMedicines.add(pm);

            prescriptionMedicineRepository.save(pm);
        });

        medicines.forEach(medicine -> {
            medicine.setPrescriptionMedicines(prescriptionMedicines);
        });

        User veterinarian = userRepository.findUsersByUserId(13);
        MedicalReport medicalReport = new MedicalReport();

        medicalReport.setConclusion("Good");
        medicalReport.setVeterinarian(veterinarian);
        medicalReport.setPrescription(prescription);
        medicalReport.setAdvise("Twice per day");

    }
}
