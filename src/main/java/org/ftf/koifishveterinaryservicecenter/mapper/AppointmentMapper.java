package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentDetailsDto;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        PaymentMapper.class,
        AddressMapper.class,
        TimeSlotMapper.class
})
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    // Map Appointment to AppointmentDto
    // Usage: mapping from DTO as body request of creating appointment
    @Mapping(source = "serviceId", target = "service.serviceId")
    @Mapping(source = "veterinarianId", target = "veterinarian.userId", conditionExpression = "java(appointmentDto.getVeterinarianId() != null)")
    @Mapping(source = "address", target = "address", conditionExpression = "java(appointmentDto.getAddress() != null)")
    @Mapping(source = "slotId", target = "timeSlot.slotId")
    @Mapping(source = "customerName", target = "customerName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "payment", target = "payment")
    Appointment convertedToAppointment(AppointmentDto appointmentDto);

    @Mapping(target = "service.description", ignore = true)
    @Mapping(source = "currentStatus", target = "currentStatus")
    @Mapping(target = "veterinarian.username", ignore = true)
    @Mapping(target = "veterinarian.password", ignore = true)
    @Mapping(target = "veterinarian.avatar", ignore = true)
    @Mapping(target = "veterinarian.address", ignore = true)
    @Mapping(target = "veterinarian.phoneNumber", ignore = true)
    AppointmentDetailsDto convertedToAppointmentDetailsDto(Appointment appointment);

    @Mapping(target = "service.description", ignore = true)
    @Mapping(source = "currentStatus", target = "currentStatus")
    @Mapping(target = "veterinarian", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "service.servicePrice", ignore = true)
    @Mapping(target = "movingSurcharge", ignore = true)
    AppointmentDetailsDto convertedToappointmentDetailsDtoForVet(Appointment appointment);
}
