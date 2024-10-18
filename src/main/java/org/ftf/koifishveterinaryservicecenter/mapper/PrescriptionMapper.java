package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.MedicineDto;
import org.ftf.koifishveterinaryservicecenter.dto.PrescriptionDto;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;
import org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine.PrescriptionMedicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = PrescriptionMapper.class)
public interface PrescriptionMapper {
    PrescriptionMapper INSTANCE = Mappers.getMapper(PrescriptionMapper.class);

    MedicineMapper MEDICINE_MAPPER = MedicineMapper.INSTANCE;

    @Mapping(source = "prescriptionMedicines", target = "allMedicine", qualifiedByName = "mapPrescriptionMedicinesToMedicineDtos")
    PrescriptionDto convertToPrescriptionDto(Prescription prescription);

    @Named("mapPrescriptionMedicinesToMedicineDtos")
    default List<MedicineDto> mapPrescriptionMedicinesToMedicineDtos(Set<PrescriptionMedicine> prescriptionMedicines) {
        return prescriptionMedicines.stream()
                .map(this::mapToMedicineDto)
                .collect(Collectors.toList());
    }

    default MedicineDto mapToMedicineDto(PrescriptionMedicine prescriptionMedicine) {
        MedicineDto medicineDto = MEDICINE_MAPPER.convertEntityToDto(prescriptionMedicine.getMedicine());
        medicineDto.setQuantity(prescriptionMedicine.getQuantity());
        return medicineDto;
    }
}
