package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.ServiceMapper;
import org.ftf.koifishveterinaryservicecenter.service.serviceservice.ServiceService;
import org.ftf.koifishveterinaryservicecenter.entity.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @CrossOrigin: Security config
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServiceService serviceServiceImpl;
    private final ServiceMapper serviceMapper;

    @Autowired
    public ServiceController(ServiceService serviceServiceImpl, ServiceMapper serviceMapper) {
        this.serviceServiceImpl = serviceServiceImpl;
        this.serviceMapper = serviceMapper;
    }

    /*
     * Return list of available services
     * */
    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {

        List<ServiceDTO> serviceDTOs = serviceServiceImpl.getAllServices();

        if (serviceDTOs.isEmpty()) { //There are no services
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
        }
    }

    @GetMapping("/{service_id}")
    public ResponseEntity<?> getServiceById(@PathVariable("service_id") Integer serviceId) {
        try {
            Service service = serviceServiceImpl.getServiceById(serviceId);
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
            ServiceDTO dto = serviceServiceImpl.updateService(serviceID, serviceFromRequest);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (AppointmentServiceNotFoundException e) { // Service not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) { // Other exceptions
            return new ResponseEntity<>("Service Update Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
