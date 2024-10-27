package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.AddressDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Address;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AddressNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.AddressMapper;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AddressController(UserService userService
            , AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /*
     * Get All address of the customer
     * Actors: Customer
     * */
    @GetMapping()
    public ResponseEntity<?> getAllAddresses() {
        try {
            User customer = userService.getCustomerById(authenticationService.getAuthenticatedUserId());
            List<Address> addresses = userService.getAllAddresses(customer.getUserId());
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
     * Get address details of the customer
     * Actors: Customer
     * */
    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddressById(
            @PathVariable("addressId") Integer addressId) {
        try {
            User customer = userService.getCustomerById(authenticationService.getAuthenticatedUserId());
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

    /*
     * Update address details of the customer
     * Actors: Customer
     * */
    @PutMapping("{addressId}")
    public ResponseEntity<?> updateAddressById(
            @PathVariable("addressId") Integer addressId
            , @RequestBody AddressDTO addressDto) {
        try {
            User customer = userService.getCustomerById(authenticationService.getAuthenticatedUserId());
            Address address = userService.getAddressById(addressId);

            if (address.getCustomer().getUserId().equals(customer.getUserId())) {
                Address newAddress = AddressMapper.INSTANCE.convertDtoToEntity(addressDto);
                address = userService.updateAddressDetails(addressId, newAddress);
                addressDto = AddressMapper.INSTANCE.convertEntityToDto(address);
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

    /*
     * Add new address into list and set as main current address of customer
     * Actors: Customer
     * */
    @PostMapping()
    public ResponseEntity<?> createAddress(
            @RequestBody AddressDTO addressDto) {
        try {
            Address address = AddressMapper.INSTANCE.convertDtoToEntity(addressDto);
            Address newAddress = userService.addAddress(authenticationService.getAuthenticatedUserId(), address);
            AddressDTO addressDTO = AddressMapper.INSTANCE.convertEntityToDto(newAddress);
            return new ResponseEntity<>(addressDTO, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Customer delete their address in the list. Can not delete current address
     * Actors: Customer
     * */
    @DeleteMapping()
    public ResponseEntity<?> deleteAddress(
            @RequestParam Integer addressId) {
        try {
            User customer = userService.getCustomerById(authenticationService.getAuthenticatedUserId());
            Address address = userService.getAddressById(addressId);
            if (address.getCustomer().getUserId().equals(customer.getUserId())) {
                if (customer.getCurrentAddress().getAddressId().equals(addressId)) {
                    return new ResponseEntity<>("The address with cannot be deleted as it is currently in use.", HttpStatus.CONFLICT);
                } else {
                    Address deletedAddress = userService.disableAddress(addressId);
                    return new ResponseEntity<>(deletedAddress, HttpStatus.OK);
                }
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
