package org.ftf.koifishveterinaryservicecenter.service.medicalreportservice;

import org.ftf.koifishveterinaryservicecenter.dto.MedicineDto;
import org.ftf.koifishveterinaryservicecenter.dto.PrescriptionDto;
import org.ftf.koifishveterinaryservicecenter.entity.Medicine;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;
import org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine.PrescriptionMedicine;
import org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine.PrescriptionMedicineId;
import org.ftf.koifishveterinaryservicecenter.exception.MedicineNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.PrescriptionNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.MedicineMapper;
import org.ftf.koifishveterinaryservicecenter.repository.MedicineRepository;
import org.ftf.koifishveterinaryservicecenter.repository.PrescriptionMedicineRepository;
import org.ftf.koifishveterinaryservicecenter.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MedicalReportServiceImpl implements MedicalReportService {

    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMedicineRepository prescriptionMedicineRepository;

    @Autowired
    public MedicalReportServiceImpl(MedicineRepository medicineRepository
            , PrescriptionRepository prescriptionRepository
            , PrescriptionMedicineRepository prescriptionMedicineRepository) {
        this.medicineRepository = medicineRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMedicineRepository = prescriptionMedicineRepository;
    }

    @Override
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    // because MedicineDto has some different properties with Medicine
    // --> flexible mapping
    @Override
    public PrescriptionDto createPrescription(PrescriptionDto prescriptionDto) {

        List<MedicineDto> medicineDtos = prescriptionDto.getAllMedicine();
        for (MedicineDto medicineDto : medicineDtos) {
            Integer medicineId = medicineDto.getMedicineId();
            Optional<Medicine> medicineInDb = medicineRepository.findById(medicineId);
            if (medicineInDb.isEmpty())
                throw new MedicineNotFoundException("Not found Medicine with id: " + medicineId);
        }

        // create a new Prescription instance
        Prescription newPrescription = new Prescription();
        newPrescription.setInstruction(prescriptionDto.getInstruction());

        // save first to generate prescription_id
        Prescription savedPrescription = prescriptionRepository.save(newPrescription);

        Set<PrescriptionMedicine> prescriptionMedicines = new HashSet<>();
        medicineDtos.forEach(dto -> {

            // set props for PrescriptionMedicine
            PrescriptionMedicineId pmId = new PrescriptionMedicineId(savedPrescription.getPrescriptionId(), dto.getMedicineId());
            PrescriptionMedicine pm = new PrescriptionMedicine(pmId, dto.getQuantity());
            pm.setPrescription(savedPrescription);
            pm.setMedicine(MedicineMapper.INSTANCE.convertDtoToEntity(dto));

            prescriptionMedicines.add(pm);
            prescriptionMedicineRepository.saveAndFlush(pm);
        });

        prescriptionDto.setPrescriptionId(savedPrescription.getPrescriptionId());
        return prescriptionDto;
    }

    @Override
    public Prescription findPrescriptionById(Integer prescriptionId) {
        Optional<Prescription> prescription = prescriptionRepository.findById(prescriptionId);
        if (prescription.isEmpty())
            throw new PrescriptionNotFoundException("Not found Prescription with id: " + prescriptionId);
        return prescription.get();
    }



}
