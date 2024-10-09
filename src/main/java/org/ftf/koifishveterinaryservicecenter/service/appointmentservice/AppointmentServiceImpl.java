package org.ftf.koifishveterinaryservicecenter.service.appointmentservice;

import org.ftf.koifishveterinaryservicecenter.entity.*;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.StatusNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.*;
import org.ftf.koifishveterinaryservicecenter.service.medicalreportservice.MedicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalReportService medicalReportService;
    private final UserRepository userRepository;
    private final MedicalReportRepository medicalReportRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository
            , MedicalReportService medicalReportService
            , UserRepository userRepository
            , MedicalReportRepository medicalReportRepository
            , StatusRepository statusRepository) {
        this.appointmentRepository = appointmentRepository;
        this.medicalReportService = medicalReportService;
        this.userRepository = userRepository;
        this.medicalReportRepository = medicalReportRepository;
        this.statusRepository = statusRepository;
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

    @Override
    public List<Status> findStatusByAppointmentId(Integer appointmentId) throws AppointmentServiceNotFoundException {

        // Get appointment by Appointment Id - Not found => exception
        Appointment appointment = this.getAppointmentById(appointmentId);

        // Get status list
        List<Status> statuses = new ArrayList<>(appointment.getStatuses());
        if(statuses.isEmpty()) {
            throw new StatusNotFoundException("Not found status logs of Appointment with id: " + appointmentId);
        }

        // Sort status by Id
        statuses.sort(Comparator.comparing(Status::getStatusId));

        return statuses;
    }

    private Appointment getAppointmentById(Integer appointmentId) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isEmpty())
            throw new AppointmentServiceNotFoundException("Not found Appointment with Id: " + appointmentId);
        return appointmentOptional.get();
    }


}
