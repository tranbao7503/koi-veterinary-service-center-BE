package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.StatusDto;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = StatusMapper.class)
public interface StatusMapper {

    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);

    StatusDto convertToStatusDto(Status status);

}
