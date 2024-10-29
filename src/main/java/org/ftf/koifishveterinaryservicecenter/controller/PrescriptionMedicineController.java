package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.MedicineDto;
import org.ftf.koifishveterinaryservicecenter.dto.PrescriptionDto;
import org.ftf.koifishveterinaryservicecenter.entity.Medicine;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;
import org.ftf.koifishveterinaryservicecenter.exception.MedicineNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.PrescriptionNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.MedicineMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.PrescriptionMapper;
import org.ftf.koifishveterinaryservicecenter.service.medicalreportservice.MedicalReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionMedicineController {

    private final MedicalReportService medicalReportService;

    public PrescriptionMedicineController(MedicalReportService medicalReportService) {
        this.medicalReportService = medicalReportService;
    }

    @PostMapping()
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        try {
            PrescriptionDto result = medicalReportService.createPrescription(prescriptionDto);
            return ResponseEntity.ok(result);
        } catch (MedicineNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/medicines")
    public ResponseEntity<List<MedicineDto>> getAllMedicines() {
        List<Medicine> medicines = medicalReportService.getAllMedicines();
        List<MedicineDto> medicineDtos = medicines.stream().map(MedicineMapper.INSTANCE::convertEntityToDto).toList();
        return ResponseEntity.ok(medicineDtos);
    }

    /*
     * View prescription
     * Actors: Manager, Customer, Veterinarian, Staff
     * */
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<?> getMedicineById(@PathVariable Integer prescriptionId) {
        try {
            Prescription prescription = medicalReportService.findPrescriptionById(prescriptionId);
            PrescriptionDto prescriptionDto = PrescriptionMapper.INSTANCE.convertToPrescriptionDto(prescription);
            return new ResponseEntity<>(prescriptionDto, HttpStatus.OK);
        } catch (PrescriptionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
