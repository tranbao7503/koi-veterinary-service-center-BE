package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.TimeSlotDto;
import org.ftf.koifishveterinaryservicecenter.dto.VeterinarianSlotsDto;
import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlots;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = AddressMapper.class)
public interface VeterinarianSlotsMapper {

    VeterinarianSlotsMapper INSTANCE = Mappers.getMapper(VeterinarianSlotsMapper.class);

    @Mapping(target = "veterinarian", ignore = true)
    VeterinarianSlotsDto convertToDto(VeterinarianSlots veterinarianSlots);

    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "veterinarians", ignore = true)
    TimeSlotDto convertToDto(TimeSlot timeSlot);
}
