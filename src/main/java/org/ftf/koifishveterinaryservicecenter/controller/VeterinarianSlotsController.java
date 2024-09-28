package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.VeterinarianSlotsDto;
import org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots.VeterinarianSlots;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.VetrinarianSlotsNotFound;
import org.ftf.koifishveterinaryservicecenter.mapper.VeterinarianSlotsMapper;
import org.ftf.koifishveterinaryservicecenter.service.slotservice.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin
@RequestMapping("/api/v1/slots")
public class VeterinarianSlotsController {
    private final SlotService slotService;

    @Autowired
    public VeterinarianSlotsController(final SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping("/{veterinarianID}")
    public ResponseEntity<?> getVeterinarianSlots(@PathVariable("veterinarianID") final Integer veterinarianID) {
        try{
            List<VeterinarianSlots> slots = slotService.getVeterinarianSlots(veterinarianID);
            List<VeterinarianSlotsDto> dtos = slots.stream()
                    .map(slot -> VeterinarianSlotsMapper.INSTANCE.convertToDto(slot))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (VetrinarianSlotsNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
