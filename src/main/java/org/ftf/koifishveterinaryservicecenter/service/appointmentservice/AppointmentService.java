package org.ftf.koifishveterinaryservicecenter.service.appointmentservice;


import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentDto;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentUpdateDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentUpdatedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppointmentService {


    void createMedicalReport(MedicalReport medicalReport,Integer appointmentId, Integer prescriptionId, Integer veterinarianId);

    List<Status> findStatusByAppointmentId(Integer appointmentId);


    Appointment getAppointmentById(Integer appointmentId);

    MedicalReport getMedicalReportByAppointmentId(Integer appointmentId);

    void createAppointment(Appointment appointment, Integer customerId);

    List<Appointment> getAppointmentsByCustomerId(Integer customerId);

    List<Appointment> getAllAppointments();

    @PreAuthorize("@appointmentServiceImpl.getAppointmentOwnerId(#appointmentId) == authentication.principal.id")
    Appointment updateAppointment(AppointmentUpdateDto appointmentDto, Integer appointmentId) throws AppointmentUpdatedException;

    void cancelAppointment(Integer appointmentId);

    void assignVeterinarian(Integer appointmentId, Integer veterinarianId);
}
