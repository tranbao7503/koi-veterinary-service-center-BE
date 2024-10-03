package org.ftf.koifishveterinaryservicecenter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = TimeSlotMapper.class, componentModel = "spring")
public interface TimeSlotMapper {

    TimeSlotMapper INSTANCE = Mappers.getMapper(TimeSlotMapper.class);

}
