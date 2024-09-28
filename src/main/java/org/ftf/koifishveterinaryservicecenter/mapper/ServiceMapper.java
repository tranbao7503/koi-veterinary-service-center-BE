package org.ftf.koifishveterinaryservicecenter.mapper;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<Service> convertToListEntity(List<ServiceDTO> serviceDTOList) {
        return serviceDTOList.stream().map(entity -> convertToService(entity)).toList();
    }

    public List<ServiceDTO> convertToListDTO(List<Service> serviceList) {
        return serviceList.stream().map(entity -> convertToServiceDTO(entity)).toList();
    }
}
