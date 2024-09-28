package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.MovingSurchargeDTO;
import org.ftf.koifishveterinaryservicecenter.entity.MovingSurcharge;
import org.ftf.koifishveterinaryservicecenter.exception.MovingSurchargeNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.MovingSurchargeMapper;
import org.ftf.koifishveterinaryservicecenter.service.surchargeservice.SurchargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin
@RequestMapping("/api/v1/surcharges")
public class SurchargeController {

    private final SurchargeService surchargeService;
    private final MovingSurchargeMapper movingSurchargeMapper;

    @Autowired
    public SurchargeController(final SurchargeService surchargeService, MovingSurchargeMapper movingSurchargeMapper) {
        this.surchargeService = surchargeService;
        this.movingSurchargeMapper = movingSurchargeMapper;
    }

    /*
     * Get all MovingSurcharge from database
     * */
    @GetMapping()
    public ResponseEntity<List<MovingSurchargeDTO>> getAllMovingSurcharges() {
        List<MovingSurcharge> movingSurcharges = surchargeService.getAllMovingSurcharges();
        if (movingSurcharges.isEmpty()) { // There are no data of moving surcharges
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else { // Data existed
            List<MovingSurchargeDTO> movingSurchargeDTOs = movingSurcharges.stream()
                    .map(movingSurchargeMapper::convertToMovingSurchargeDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(movingSurchargeDTOs, HttpStatus.OK);
        }
    }

    /*
     * Get Moving Surcharge by ID
     * */
    @GetMapping("/{surchargeID}")
    public ResponseEntity<?> getMovingSurcharge(
            @PathVariable("surchargeID") Integer surchargeID) {
        try {
            MovingSurcharge movingSurcharge = surchargeService.getMovingSurchargeById(surchargeID);
            MovingSurchargeDTO movingSurchargeDTO = movingSurchargeMapper.convertToMovingSurchargeDTO(movingSurcharge);
            return new ResponseEntity<>(movingSurchargeDTO, HttpStatus.OK);
        } catch (MovingSurchargeNotFoundException e) { // Moving surcharge not existed
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
    * Update price of a moving surcharge
    * */
    @PutMapping("/{surchargeID}")
    public ResponseEntity<?> updateMovingSurcharge(
            @PathVariable("surchargeID") Integer surchargeID,
            @RequestBody MovingSurchargeDTO movingSurchargeFromRequest) {
        try {
            MovingSurcharge movingSurcharge = movingSurchargeMapper.convertToMovingSurcharge(movingSurchargeFromRequest);
            surchargeService.updateMovingSurcharge(surchargeID, movingSurcharge);
            return new ResponseEntity<>(movingSurchargeFromRequest, HttpStatus.OK);
        } catch (MovingSurchargeNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Moving Surcharge Update Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
