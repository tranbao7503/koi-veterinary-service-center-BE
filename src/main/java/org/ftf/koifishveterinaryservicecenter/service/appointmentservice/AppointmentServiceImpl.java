package org.ftf.koifishveterinaryservicecenter.service.appointmentservice;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.repository.MedicalReportRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.ftf.koifishveterinaryservicecenter.service.medicalreportservice.MedicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalReportService medicalReportService;
    private final UserRepository userRepository;
    private final MedicalReportRepository medicalReportRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, MedicalReportService medicalReportService, UserRepository userRepository, MedicalReportRepository medicalReportRepository) {
        this.appointmentRepository = appointmentRepository;
        this.medicalReportService = medicalReportService;
        this.userRepository = userRepository;
        this.medicalReportRepository = medicalReportRepository;
    }


    @Override
    public void createMedicalReport(MedicalReport medicalReport, Integer appointmentId, Integer prescriptionId, Integer veterinarianId) {

        // get prescription on db by id
        Prescription prescription = medicalReportService.findPrescriptionById(prescriptionId);

        // set prop for medicalReport
        medicalReport.setPrescription(prescription);

        User veterinarian = userRepository.findVeterinarianById(veterinarianId);
        medicalReport.setVeterinarian(veterinarian);

        MedicalReport savedMedicalReport = medicalReportRepository.save(medicalReport);

        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setMedicalReport(savedMedicalReport);

        appointmentRepository.save(appointment);
    }

    private Appointment getAppointmentById(Integer appointmentId) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isEmpty())
            throw new AppointmentServiceNotFoundException("Not found Appointment with Id: " + appointmentId);
        return appointmentOptional.get();
    }


}
