package org.ftf.koifishveterinaryservicecenter.service;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.model.Service;
import org.ftf.koifishveterinaryservicecenter.repository.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.ServiceNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    private ModelMapper modelMapper;
    private ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ModelMapper modelMapper) {
        this.serviceRepository = serviceRepository;
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

    /*
     * Get all available services
     * */
    public List<ServiceDTO> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        List<ServiceDTO> serviceDTOs = services.stream()
                .map(this::convertToServiceDTO)
                .collect(Collectors.toList());
        return serviceDTOs;
    }


    /*
    * Update price of service
    * */
    public void updateServicePrice(Integer serviceId, BigDecimal newPrice) throws ServiceNotFoundException {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException("Service Not Found With ID: " + serviceId)); // Throw exception whether service not found

        service.setServicePrice(newPrice); // Update price
        serviceRepository.save(service); // Save to database
    }
}
