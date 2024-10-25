package org.ftf.koifishveterinaryservicecenter.presciptionmedicine;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.Medicine;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;
import org.ftf.koifishveterinaryservicecenter.repository.PrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class PrescriptionRepositoryTests {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Test
    public void testAddPrescriptionSuccess() {
        Medicine medicine = new Medicine();
        medicine.setMedicineId(1);
        medicine.setMedicineName("Antibiotic A");
        Medicine medicine2 = new Medicine();
        medicine2.setMedicineId(2);
        medicine2.setMedicineName("Antibiotic B");

        Set<Medicine> medicines = Set.of(medicine, medicine2);

        Prescription prescription = new Prescription();
        prescription.setInstruction("Use antibiotic cream twice daily.");
        //   prescription.setAllMedicine(medicines);

        Prescription storedPrescription = prescriptionRepository.save(prescription);
        Assertions.assertThat(storedPrescription).isNotNull();
    }

}
