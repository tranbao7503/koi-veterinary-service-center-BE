package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.MovingSurchargeDTO;
import org.ftf.koifishveterinaryservicecenter.entity.MovingSurcharge;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = MovingSurchargeMapper.class)
public interface MovingSurchargeMapper {

    MovingSurchargeMapper INSTANCE = Mappers.getMapper(MovingSurchargeMapper.class);

    MovingSurchargeDTO convertToMovingSurchargeDto(MovingSurcharge movingSurcharge);

    MovingSurcharge convertToMovingSurchargeEntity(MovingSurchargeDTO movingSurchargeDTO);
}
