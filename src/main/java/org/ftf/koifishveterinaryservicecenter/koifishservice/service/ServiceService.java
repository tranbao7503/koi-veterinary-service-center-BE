package org.ftf.koifishveterinaryservicecenter.koifishservice.service;

import org.ftf.koifishveterinaryservicecenter.koifishservice.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.koifishservice.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.koifishservice.mapper.ServiceMapper;
import org.ftf.koifishveterinaryservicecenter.model.Service;
import org.ftf.koifishveterinaryservicecenter.repository.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.ServiceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    /*
     * Get all available services
     * */
    public List<ServiceDTO> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        List<ServiceDTO> serviceDTOs = services.stream()
                .map(serviceMapper::convertToServiceDTO)
                .collect(Collectors.toList());
        return serviceDTOs;
    }


    public Service getServiceById(Integer serviceId) {
        Service service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) throw new AppointmentServiceNotFoundException("Service not found with ID: " + serviceId);
        return service;
    }


    /*
     * Update price of service
     * */
    public ServiceDTO updateService(Integer serviceId, ServiceDTO serviceFromRequest) throws ServiceNotFoundException {

        // convert dto -> entity
        Service convertedService = serviceMapper.convertToService(serviceFromRequest);

        // check service existed from db
        Service serviceFromDb = serviceRepository.findById(serviceId).orElse(null);
        if (serviceFromDb == null) {
            throw new AppointmentServiceNotFoundException("Service not found with ID: " + serviceId);
        }
        ServiceDTO result = serviceMapper.convertToServiceDTO(serviceRepository.save(convertedService));
        return result;
    }
}
