package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.AppointmentDto;
import org.ftf.koifishveterinaryservicecenter.dto.TimeSlotDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(uses = TimeSlotMapper.class, componentModel = "spring")
public interface TimeSlotMapper {

    TimeSlotMapper INSTANCE = Mappers.getMapper(TimeSlotMapper.class);

    // Map TimeSlot to TimeSlotDto, using a custom method to map the first appointment
    @Mapping(source = "appointments", target = "appointment", qualifiedByName = "mapFirstAppointment")
//    @Mapping(target = "appointment.timeSlot", ignore = true)
    TimeSlotDto convertToTimeSlotDto(TimeSlot timeSlot);

    // A custom method to extract the first appointment from the set and map it to AppointmentDto
    @Named("mapFirstAppointment")
    default AppointmentDto mapFirstAppointment(Set<Appointment> appointments) {
        return appointments.stream().findFirst()
                .map(appointment -> AppointmentMapper.INSTANCE.convertToAppointmentDto(appointment))
                .orElse(null); // Handle the case when no appointments exist
    }

}
