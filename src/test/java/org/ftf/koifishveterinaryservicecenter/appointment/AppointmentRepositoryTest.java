package org.ftf.koifishveterinaryservicecenter.appointment;

import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.repository.MedicalReportRepository;
import org.ftf.koifishveterinaryservicecenter.repository.PrescriptionRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MedicalReportRepository medicalReportRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testCreateMedicalReportSuccess() {
        int prescriptionId = 82;
        Optional<Prescription> prescription = prescriptionRepository.findById(prescriptionId);

        MedicalReport medicalReport = new MedicalReport();
        medicalReport.setConclusion("Pond temperature fluctuation noted.");
        medicalReport.setAdvise("Install heaters for temperature control.");

        medicalReport.setPrescription(prescription.get());

        User veterinarian = userRepository.findVeterinarianById(3);
        medicalReport.setVeterinarian(veterinarian);

        medicalReportRepository.save(medicalReport);
    }


}
