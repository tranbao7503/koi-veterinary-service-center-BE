package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Service;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.ServiceMapper;
import org.ftf.koifishveterinaryservicecenter.service.serviceservice.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
// @CrossOrigin: Security config
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServiceService serviceServiceImpl;

    @Autowired
    public ServiceController(ServiceService serviceServiceImpl) {
        this.serviceServiceImpl = serviceServiceImpl;
    }

    /*
     * Return list of available services
     * */
    @GetMapping
    public ResponseEntity<?> getAllServices() {

        List<Service> services = serviceServiceImpl.getAllServices();


        if (services.isEmpty()) { //There are no services
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Mapping to DTOs
            List<ServiceDTO> serviceDTOs = services.stream()
                    .map(ServiceMapper.INSTANCE::convertToServiceDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
        }
    }

    /*
     * Find a service by ID
     * */
    @GetMapping("/{serviceID}")
    public ResponseEntity<?> getServiceById(@PathVariable("serviceID") Integer serviceId) {
        try {
            Service service = serviceServiceImpl.getServiceById(serviceId);
            ServiceDTO serviceDTO = ServiceMapper.INSTANCE.convertToServiceDto(service);
            return new ResponseEntity<>(serviceDTO, HttpStatus.OK);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
     * Update price of a service
     * */
    @PutMapping("/{serviceID}")

    public ResponseEntity<?> updateService(
            @PathVariable("serviceID") Integer serviceID,
            @RequestBody ServiceDTO serviceFromRequest) {
        try {
            Service service = ServiceMapper.INSTANCE.convertToServiceEntity(serviceFromRequest);
            serviceServiceImpl.updateService(serviceID, service);
            return new ResponseEntity<>(serviceFromRequest, HttpStatus.OK);
        } catch (AppointmentNotFoundException e) { // Service not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) { // Other exceptions
            return new ResponseEntity<>("Service Update Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
