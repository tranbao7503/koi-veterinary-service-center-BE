package org.ftf.koifishveterinaryservicecenter.koifishservice.mapper;

import org.ftf.koifishveterinaryservicecenter.koifishservice.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.koifishservice.entity.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ServiceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /*
     * Convert Service entity to Service DTO
     * */
    public ServiceDTO convertToServiceDTO(Service service) {
        return modelMapper.map(service, ServiceDTO.class);
    }

    /*
     * Convert Service DTO to Service entity
     * */
    public Service convertToService(ServiceDTO serviceDTO) {
        return modelMapper.map(serviceDTO, Service.class);
    }
}
