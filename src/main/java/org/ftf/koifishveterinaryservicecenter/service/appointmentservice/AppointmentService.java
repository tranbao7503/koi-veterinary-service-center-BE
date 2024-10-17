package org.ftf.koifishveterinaryservicecenter.service.appointmentservice;


import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Feedback;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Status;

import java.util.List;

public interface AppointmentService {


    void createMedicalReport(MedicalReport medicalReport,Integer appointmentId);

    List<Status> findStatusByAppointmentId(Integer appointmentId);

    Appointment getAppointmentById(Integer appointmentId);

    MedicalReport getMedicalReportByAppointmentId(Integer appointmentId);

    void createAppointment(Appointment appointment, Integer customerId);

    List<Appointment> getAppointmentsByCustomerId(Integer customerId);

    List<Appointment> getAllAppointments();

    Feedback createFeedback(Integer appointmentId, Feedback feedback);

}
