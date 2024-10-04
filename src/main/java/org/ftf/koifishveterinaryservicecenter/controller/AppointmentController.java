package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.MedicalReportDto;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.PrescriptionNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.MedicalReportMapper;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.medicalreportservice.MedicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {


    private final MedicalReportService medicalReportService;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(MedicalReportService medicalReportService, AppointmentService appointmentService) {
        this.medicalReportService = medicalReportService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/{appointmentId}/report")
    public ResponseEntity<?> createMedicalReport(@PathVariable Integer appointmentId, @RequestBody MedicalReportDto reportDto) {
        // Check veterinarianId == userId from Authentication (in SecurityContext)
        int userIdFromContext = 3;

        Integer veterinarianId = reportDto.getVeterinarianId();
        Integer prescriptionId = reportDto.getPrescriptionId();

        if(veterinarianId == null || prescriptionId == null) {
            return ResponseEntity.badRequest().body("Veterinarian id or prescription id is required !!");
        }

        if (userIdFromContext != veterinarianId) {
            return ResponseEntity.badRequest().body("Invalid veterinarian Id");
        }

        try {
            MedicalReport convertedMedicalReport = MedicalReportMapper.INSTANCE.convertToEntity(reportDto);
            appointmentService.createMedicalReport(convertedMedicalReport, appointmentId, prescriptionId, veterinarianId);
            return ResponseEntity.ok().body("Added medical report successfully");
        } catch (PrescriptionNotFoundException | AppointmentServiceNotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

}
