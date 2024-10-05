package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.AppointmentDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = AppointmentMapper.class, componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    // Map Appointment to AppointmentDto
    @Mapping(source = "service.serviceName", target = "serviceName")
    @Mapping(source = "currentStatus", target = "currentStatus")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "customerName", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "timeSlot", ignore = true)
    AppointmentDto convertToAppointmentDto(Appointment appointment);
}
