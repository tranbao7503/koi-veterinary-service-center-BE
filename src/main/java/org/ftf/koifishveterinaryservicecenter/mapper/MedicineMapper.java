package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.MedicineDto;
import org.ftf.koifishveterinaryservicecenter.entity.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;


@Mapper
public interface MedicineMapper {

    MedicineMapper INSTANCE = Mappers.getMapper(MedicineMapper.class);

    @Mappings({
            @Mapping(source = "medicineId", target = "medicineId"),
            @Mapping(source = "medicineName", target = "medicineName")
    })
    Medicine convertDtoToEntity(MedicineDto medicineDto);

    @Mappings({
            @Mapping(source = "medicineId", target = "medicineId"),
            @Mapping(source = "medicineName", target = "medicineName")
    })
    MedicineDto convertEntityToDto(Medicine medicine);

}
