package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.MovingSurchargeDTO;
import org.ftf.koifishveterinaryservicecenter.exception.MovingSurchargeNotFoundException;
import org.ftf.koifishveterinaryservicecenter.service.surchargeservice.SurchargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/api/v1/Surcharges")
public class SurchargeController {

    private final SurchargeService surchargeService;

    @Autowired
    public SurchargeController(final SurchargeService surchargeService) {
        this.surchargeService = surchargeService;
    }

    /*
     * Return all MovingSurcharge from database
     * */
    @GetMapping("")
    public ResponseEntity<List<MovingSurchargeDTO>> getAllMovingSurcharges() {
        List<MovingSurchargeDTO> movingSurcharges = surchargeService.getAllMovingSurcharges();
        if (movingSurcharges.isEmpty()) { // There are no data of moving surcharges
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else { // Data existed
            return new ResponseEntity<>(movingSurcharges, HttpStatus.OK);
        }
    }

    /*
     * Get Moving Surcharge by ID
     * */
    @GetMapping("{surchargeID}")
    public ResponseEntity<?> getMovingSurcharge(
            @PathVariable("surchargeID") Integer surchargeID) {
        try {
            MovingSurchargeDTO movingSurchargeDTO = surchargeService.getMovingSurchargeById(surchargeID);
            return new ResponseEntity<>(movingSurchargeDTO, HttpStatus.OK);
        } catch (MovingSurchargeNotFoundException e) { // Moving surcharge not existed
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
    * Update price of a moving surcharge
    * */
    @PutMapping("{surchargeID}")
    public ResponseEntity<?> updateMovingSurcharge(
            @PathVariable("surchargeID") Integer surchargeID,
            @RequestBody MovingSurchargeDTO movingSurchargeFromRequest) {
        try {
            MovingSurchargeDTO movingSurchargeDTO = surchargeService.updateMovingSurcharge(surchargeID, movingSurchargeFromRequest);
            return new ResponseEntity<>(movingSurchargeDTO, HttpStatus.OK);
        } catch (MovingSurchargeNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Moving Surcharge Update Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
