package org.ftf.koifishveterinaryservicecenter.service.appointmentservice;


import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;

public interface AppointmentService {
    void createMedicalReport(MedicalReport medicalReport, Integer appointmentId, Integer prescriptionId, Integer veterinarianId);
}
