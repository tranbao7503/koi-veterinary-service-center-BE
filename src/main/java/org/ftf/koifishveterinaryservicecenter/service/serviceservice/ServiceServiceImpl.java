package org.ftf.koifishveterinaryservicecenter.service.serviceservice;

import org.ftf.koifishveterinaryservicecenter.entity.Service;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.ServiceMapper;
import org.ftf.koifishveterinaryservicecenter.repository.ServiceRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {


    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;


    @Autowired
    public ServiceServiceImpl(UserRepository userRepository, ServiceRepository serviceRepository, ServiceMapper serviceMapper, ModelMapper modelMapper, ServiceRepository serviceRepository1) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository1;
    }

    /*
     * Get all available services
     * */
    public List<Service> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return services;
    }


    @Override
    public Service getServiceById(Integer serviceId) throws AppointmentServiceNotFoundException {
        Service service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) {
            throw new AppointmentServiceNotFoundException("Service not found with ID: " + serviceId);
        }
        return service;
    }


    /*
     * Update price of service
     * */
    @Override
    public Service updateService(Integer serviceId, Service serviceFromRequest) throws AppointmentServiceNotFoundException {

        // check service existed from db
        Service serviceFromDb = serviceRepository.findById(serviceId).orElse(null);
        if (serviceFromDb == null) {
            throw new AppointmentServiceNotFoundException("Service not found with ID: " + serviceId);
        }
        serviceFromDb = serviceRepository.save(serviceFromRequest);
        return serviceFromDb;

    }





}


