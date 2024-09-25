package org.ftf.koifishveterinaryservicecenter.koifishservice.controller;

import org.ftf.koifishveterinaryservicecenter.koifishservice.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.koifishservice.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.koifishservice.mapper.ServiceMapper;
import org.ftf.koifishveterinaryservicecenter.koifishservice.service.ServiceService;
import org.ftf.koifishveterinaryservicecenter.koifishservice.entity.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @CrossOrigin: Security config
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServiceService serviceService;
    private final ServiceMapper serviceMapper;

    @Autowired
    public ServiceController(ServiceService serviceService, ServiceMapper serviceMapper) {
        this.serviceService = serviceService;
        this.serviceMapper = serviceMapper;
    }

    /*
     * Return list of available services
     * */
    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {

        List<ServiceDTO> serviceDTOs = serviceService.getAllServices();

        if (serviceDTOs.isEmpty()) { //There are no services
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
        }
    }

    @GetMapping("/{service_id}")
    public ResponseEntity<?> getServiceById(@PathVariable("service_id") Integer serviceId) {
        try {
            Service service = serviceService.getServiceById(serviceId);
            ServiceDTO serviceDTO = serviceMapper.convertToServiceDTO(service);
            return new ResponseEntity<>(serviceDTO, HttpStatus.OK);
        } catch (AppointmentServiceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
     * Update price of a service
     * */
    @PutMapping("/{serviceID}")
    public ResponseEntity<?> updateServicePrice(
            @PathVariable("serviceID") Integer serviceID,
            @RequestBody ServiceDTO serviceFromRequest) {

        try {
            ServiceDTO dto = serviceService.updateService(serviceID, serviceFromRequest);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (AppointmentServiceNotFoundException e) { // Service not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) { // Other exceptions
            return new ResponseEntity<>("Service Update Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
