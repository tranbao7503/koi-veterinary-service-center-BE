package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.MedicalReportDto;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MedicalReportMapper {

    MedicalReportMapper INSTANCE = Mappers.getMapper(MedicalReportMapper.class);

    @Mappings({
            @Mapping(source = "reportId", target = "reportId"),
            @Mapping(source = "conclusion", target = "conclusion"),
            @Mapping(source = "advise", target = "advise")
    })
    MedicalReport convertToEntity(MedicalReportDto medicalReportDto);

    @Mappings({
            @Mapping(source = "reportId", target = "reportId"),
            @Mapping(source = "conclusion", target = "conclusion"),
            @Mapping(source = "advise", target = "advise"),
            @Mapping(source = "veterinarian.userId",target = "veterinarianId"),
            @Mapping(source = "prescription.prescriptionId", target = "prescriptionId")
    })
    MedicalReportDto convertToDto(MedicalReport medicalReport);

}
