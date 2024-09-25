package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.MovingSurchargeDTO;
import org.ftf.koifishveterinaryservicecenter.entity.MovingSurcharge;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovingSurchargeMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public MovingSurchargeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /*
    * Convert MovingSurcharge entity to MovingSurcharge DTO
    * */
    public MovingSurchargeDTO convertToMovingSurchargeDTO(MovingSurcharge movingSurcharge) {
        return modelMapper.map(movingSurcharge, MovingSurchargeDTO.class);
    }

    /*
     * Convert MovingSurcharge DTO to MovingSurcharge entity
     * */
    public MovingSurcharge convertToMovingSurcharge(MovingSurchargeDTO movingSurchargeDTO) {
        return modelMapper.map(movingSurchargeDTO, MovingSurcharge.class);
    }
}
