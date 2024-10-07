package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.MedicalReportDto;
import org.ftf.koifishveterinaryservicecenter.dto.StatusDto;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.PrescriptionNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.StatusNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.MedicalReportMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.StatusMapper;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.medicalreportservice.MedicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/{appointmentId}/logs")
    public ResponseEntity<?> getAppointmentLogs(@PathVariable("appointmentId") Integer appointmentId) {
        try{
            List<Status> statuses = appointmentService.findStatusByAppointmentId(appointmentId);
            List<StatusDto> statusDtos = statuses.stream()
                    .map(StatusMapper.INSTANCE::convertToStatusDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(statusDtos, HttpStatus.OK);
        } catch (AppointmentServiceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StatusNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
        }

    }

}
