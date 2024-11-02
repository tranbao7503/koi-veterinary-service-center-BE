package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.SlotStatusDto;
import org.ftf.koifishveterinaryservicecenter.dto.TimeSlotDto;
import org.ftf.koifishveterinaryservicecenter.dto.UserDTO;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.TimeSlot;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.SlotStatus;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.SlotStatusException;
import org.ftf.koifishveterinaryservicecenter.exception.TimeSlotNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.TimeSlotMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.UserMapper;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.slotservice.SlotService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin
@RequestMapping("/api/v1/slots")
public class TimeSlotController {

    private final SlotService slotService;
    private final TimeSlotMapper timeSlotMapper;
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public TimeSlotController(final SlotService slotService, TimeSlotMapper timeSlotMapper, AppointmentService appointmentService, UserService userService, AuthenticationService authenticationService) {
        this.slotService = slotService;
        this.timeSlotMapper = timeSlotMapper;
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /*
     * Get BOOKED slot of a veterinarian
     * Actors: Veterinarian
     * */
    @GetMapping("/{veterinarianID}")
    public ResponseEntity<?> getVeterinarianSlots(@PathVariable("veterinarianID") final Integer veterinarianID) {
        try {
            List<TimeSlot> slots = slotService.getVeterinarianSlots(veterinarianID);
            List<TimeSlotDto> dtos = slots.stream().map(TimeSlotMapper.INSTANCE::convertToTimeSlotDto).collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (TimeSlotNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get all available slot id > current hour + 3h
    // for Customer
//    @GetMapping("/available")
//    public ResponseEntity<?> getAvailableSlots() {
//        List<TimeSlot> timeSlots = slotService.getListAvailableTimeSlots();
//        List<TimeSlotDto> dtos = timeSlots.stream().map(timeSlotMapper::convertToTimeSlotDtoAvailable).toList();
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }

    /*
     * Get all available slots since current day for customer for choosing in case not specifying veterinarian
     * Actors: Customer
     * */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSlots() {
        try {
            List<TimeSlot> slots = slotService.getAvailableSlots();
            List<TimeSlotDto> slotDtos = slots.stream().map(TimeSlotMapper.INSTANCE::convertToAvailableTimeSlotDto).collect(Collectors.toList());
            return new ResponseEntity<>(slotDtos, HttpStatus.OK);
        } catch (TimeSlotNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
     * Get all available slots since current day for customer for choosing in case not specifying veterinarian
     * Actors: Customer
     * */
    @GetMapping("/{veterinarianId}/available")
    public ResponseEntity<?> getAvailableSlotsByVeterinarianId(@PathVariable("veterinarianId") final Integer veterinarianId) {
        try {
            List<TimeSlot> slots = slotService.getAvailableSlotsByVeterinarianId(veterinarianId);
            List<TimeSlotDto> slotDtos = slots.stream().map(TimeSlotMapper.INSTANCE::convertToAvailableTimeSlotDto).collect(Collectors.toList());
            return new ResponseEntity<>(slotDtos, HttpStatus.OK);
        } catch (TimeSlotNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Get all available slots come after the slot of the main appointment
     * Actors: Veterinarian
     * */
    @GetMapping("/{veterinarianId}/follow-up-appointment")
    public ResponseEntity<?> getAvailableSlotsForFollowUpAppointment(@PathVariable("veterinarianId") final Integer veterinarianId, @RequestParam("appointmentId") Integer appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            List<TimeSlot> slots = slotService.getAvailableSlotForFollowUpAppointment(veterinarianId, appointment.getTimeSlot().getSlotId());
            List<TimeSlotDto> slotDtos = slots.stream().map(TimeSlotMapper.INSTANCE::convertToAvailableTimeSlotDto).collect(Collectors.toList());
            return new ResponseEntity<>(slotDtos, HttpStatus.OK);
        } catch (TimeSlotNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/booked")
    public ResponseEntity<?> getBookedSlots() {
        try {
            List<TimeSlot> timeSlots = slotService.getBookedTimeSlots();
            List<TimeSlotDto> slotDtos = timeSlots.stream().map(TimeSlotMapper.INSTANCE::convertToAvailableTimeSlotDto).collect(Collectors.toList());

            for (TimeSlotDto slotDto : slotDtos) { // Map veterinarians into slots
                List<User> bookedVeterinarians = userService.getBookedVeterinarianBySlotId(slotDto.getSlotId());
                List<UserDTO> veterinarianDtos = bookedVeterinarians.stream().map(UserMapper.INSTANCE::convertToVeterinarianDto).collect(Collectors.toList());
                slotDto.setBookedVeterinarian(veterinarianDtos);
            }

            return new ResponseEntity<>(slotDtos, HttpStatus.OK);
        } catch (TimeSlotNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{slotId}")
    public ResponseEntity<?> updateTimeSlot(@RequestBody SlotStatusDto slotStatusDto, @PathVariable Integer slotId) {
        try {
            Integer veterinarianId = authenticationService.getAuthenticatedUserId();
            User veterinarian = userService.getVeterinarianById(veterinarianId);

            SlotStatus updatedSlotStatus = SlotStatus.valueOf(slotStatusDto.getStatus().toUpperCase());
            if (updatedSlotStatus.equals(SlotStatus.UNAVAILABLE)) {
                slotService.updateVeterinarianSlotsStatus(veterinarian.getUserId(), slotId, updatedSlotStatus);
                appointmentService.unAssignedVeterinarianOnSlot(veterinarian.getUserId(), slotId);
            }

            return new ResponseEntity<>("Update slot status successfully !", HttpStatus.OK);
        } catch (SlotStatusException e) {
            return new ResponseEntity<>("Invalid update slot status: " + slotStatusDto, HttpStatus.BAD_REQUEST);
        }
    }

}
