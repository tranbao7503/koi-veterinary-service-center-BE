package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.AddressDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AddressNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.AddressMapper;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final UserService userService;

    @Autowired
    public AddressController(UserService userService) {
        this.userService = userService;
    }

    /*
     * Get All address of the customer
     * Actors: Customer
     * */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAllAddresses(
            @PathVariable("customerId") Integer customerId) {
        try {
            List<Address> addresses = userService.getAllAddresses(customerId);
            List<AddressDTO> addressDtoList = addresses.stream()
                    .map(AddressMapper.INSTANCE::convertEntityToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(addressDtoList, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AddressNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Get All address details of the customer
     * Actors: Customer
     * */
    @GetMapping("/{addressId}/customer/{customerId}")
    public ResponseEntity<?> getAddressById(
            @PathVariable("addressId") Integer addressId
            , @PathVariable("customerId") Integer customerId) {
        try {
            User customer = userService.getCustomerById(customerId);
            Address address = userService.getAddressById(addressId);
            if (address.getCustomer().getUserId().equals(customer.getUserId())) {
                AddressDTO addressDto = AddressMapper.INSTANCE.convertEntityToDto(address);
                return new ResponseEntity<>(addressDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AddressNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
