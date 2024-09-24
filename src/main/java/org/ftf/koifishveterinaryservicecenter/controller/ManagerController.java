package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.ServiceDTO;
import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.service.ServiceService;
import org.ftf.koifishveterinaryservicecenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.ServiceNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/manager")
public class ManagerController {

    private ServiceService serviceService;
    private UserService userService;

    @Autowired
    public ManagerController(ServiceService serviceService, UserService userService) {
        this.serviceService = serviceService;
        this.userService = userService;
    }

    /*
     * Return list of available services
     * */
    @GetMapping("services")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {

        List<ServiceDTO> serviceDTOs = serviceService.getAllServices();

        if(serviceDTOs.isEmpty()) { //There are no services
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(serviceDTOs, HttpStatus.OK);
        }
    }

    /*
    * Update price of a service
    * */
   /* @PutMapping("services/{serviceID}/update-price")
    public ResponseEntity<String> updateServicePrice(
            @PathVariable("serviceID") Integer serviceID,
            @RequestBody ServiceDTO serviceDTO) {

        try {
            BigDecimal price = serviceDTO.getServicePrice();
            serviceService.updateServicePrice(serviceID, price);
            return new ResponseEntity<>("Price Updated Successfully", HttpStatus.OK);
        } catch (ServiceNotFoundException e) { // Service not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) { // Other exceptions
            return new ResponseEntity<>("Service Update Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @GetMapping("customers")
    public ResponseEntity<List<UserDTO>> getAllCustomers(){
        List<UserDTO> customers=userService.getAllCustomers();

        if(customers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(customers,HttpStatus.OK);
        }
    }



}
