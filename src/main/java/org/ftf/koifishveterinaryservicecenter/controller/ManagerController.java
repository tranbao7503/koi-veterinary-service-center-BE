package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.model.Service;
import org.ftf.koifishveterinaryservicecenter.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/manager")
public class ManagerController {

    private ServiceService serviceService;

    @Autowired
    public ManagerController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /*
    * Return list of available services
    * */
    @GetMapping("service-list")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceDTO> serviceDTOs = serviceService.getAllServices();
        return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
    }
}
