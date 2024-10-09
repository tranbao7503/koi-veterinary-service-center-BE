package org.ftf.koifishveterinaryservicecenter.service.appointmentservice;


import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Status;

import java.util.List;

public interface AppointmentService {

    void createMedicalReport(MedicalReport medicalReport,Integer appointmentId, Integer prescriptionId, Integer veterinarianId);
    List<Status> findStatusByAppointmentId(Integer appointmentId);

}
