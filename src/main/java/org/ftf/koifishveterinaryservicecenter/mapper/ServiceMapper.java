package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = ServiceMapper.class)
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    ServiceDTO convertToServiceDto(Service service);

    Service convertToServiceEntity(ServiceDTO serviceDTO);


}
