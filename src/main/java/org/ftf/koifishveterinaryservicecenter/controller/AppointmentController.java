package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.MedicalReportDto;
import org.ftf.koifishveterinaryservicecenter.dto.StatusDto;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentDetailsDto;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentDto;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentForListDto;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentUpdateDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.MedicalReport;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.*;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentDto;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.PrescriptionNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.StatusNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.AppointmentMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.MedicalReportMapper;
import org.ftf.koifishveterinaryservicecenter.mapper.StatusMapper;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {


    private final AppointmentService appointmentService;
    private final AuthenticationService authenticationService;
    private final UserService userService;


    @Autowired
    public AppointmentController(AppointmentService appointmentService, AuthenticationService authenticationService, UserService userService) {
        this.appointmentService = appointmentService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    // for Veterinarian
    @PostMapping("/{appointmentId}/report")
    public ResponseEntity<?> createMedicalReport(@PathVariable Integer appointmentId, @RequestBody MedicalReportDto reportDto) {
        // Check veterinarianId == userId from Authentication (in SecurityContext)
        int userIdFromContext = 3;

        Integer veterinarianId = reportDto.getVeterinarianId();
        Integer prescriptionId = reportDto.getPrescriptionId();

        if (veterinarianId == null || prescriptionId == null) {
            return ResponseEntity.badRequest().body("Veterinarian id or prescription id is required !!");
        }

        if (userIdFromContext != veterinarianId) {
            return ResponseEntity.badRequest().body("Invalid veterinarian Id");
        }

        try {
            MedicalReport convertedMedicalReport = MedicalReportMapper.INSTANCE.convertToEntity(reportDto);
            appointmentService.createMedicalReport(convertedMedicalReport, appointmentId, prescriptionId, veterinarianId);
            return ResponseEntity.ok().body("Added medical report successfully");
        } catch (PrescriptionNotFoundException | AppointmentNotFoundException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /*
     * Actors: Manager
     * */
    @GetMapping("/{appointmentId}/logs")
    public ResponseEntity<?> getAppointmentLogs(@PathVariable("appointmentId") Integer appointmentId) {
        try {
            List<Status> statuses = appointmentService.findStatusByAppointmentId(appointmentId);
            List<StatusDto> statusDtos = statuses.stream().map(StatusMapper.INSTANCE::convertToStatusDto).collect(Collectors.toList());
            return new ResponseEntity<>(statusDtos, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StatusNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    /*
     * Actors: Customer, Veterinarian, Manager
     * */
    @GetMapping("/{appointmentId}/report")
    public ResponseEntity<?> getAppointmentReport(@PathVariable("appointmentId") Integer appointmentId) {
        try {
            User user = userService.getUserProfile(authenticationService.getAuthenticatedUserId());

            Appointment appointment = appointmentService.getAppointmentById(appointmentId);

            MedicalReport medicalReport = appointmentService.getMedicalReportByAppointmentId(appointmentId);

            if (user.getRole().getRoleKey().equals("CUS")) { // Validate customer
                if(!appointment.getCustomer().getUserId().equals(user.getUserId())) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

            if (user.getRole().getRoleKey().equals("VET")) { // validate veterinarian
                if(!medicalReport.getVeterinarian().getUserId().equals(user.getUserId())) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

            MedicalReportDto medicalReportDto = MedicalReportMapper.INSTANCE.convertToDto(medicalReport);
            return new ResponseEntity<>(medicalReportDto, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (MedicalReportNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // for Customer
    @PostMapping()
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        Integer userId = authenticationService.getAuthenticatedUserId();
        try {
            Appointment convertedAppointment = AppointmentMapper.INSTANCE.convertedToAppointment(appointmentDto);
            appointmentService.createAppointment(convertedAppointment, userId);
            return new ResponseEntity<>("Booked an appointment successfully", HttpStatus.CREATED);
        } catch (UserNotFoundException | AddressNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
     * Actors: Staff, Manager
     * */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointment(@PathVariable("appointmentId") Integer appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            AppointmentDetailsDto appointmentDetailsDto = AppointmentMapper.INSTANCE.convertedToAppointmentDetailsDto(appointment);
            return new ResponseEntity<>(appointmentDetailsDto, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    /*
     * Actors: Veterinarian
     * */
    @GetMapping("/{appointmentId}/veterinarian")
    public ResponseEntity<?> getAppointmentForVeterinarian(
            @PathVariable("appointmentId") Integer appointmentId) {
        try {
            User veterinarian = userService.getVeterinarianById(authenticationService.getAuthenticatedUserId());
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            if (appointment.getVeterinarian().getUserId().equals(veterinarian.getUserId())) {
                AppointmentDetailsDto appointmentDetailsDto = AppointmentMapper.INSTANCE.convertedToAppointmentDetailsDtoForVet(appointment);
                return new ResponseEntity<>(appointmentDetailsDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AppointmentNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Actors: Customer
     * */

    @GetMapping("/{appointmentId}/customer")
    public ResponseEntity<?> getAppointmentForCustomer(
            @PathVariable("appointmentId") Integer appointmentId) {
        try {
            User customer = userService.getCustomerById(authenticationService.getAuthenticatedUserId());
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            if (appointment.getCustomer().getUserId().equals(customer.getUserId())) {
                AppointmentDetailsDto appointmentDetailsDto = AppointmentMapper.INSTANCE.convertedToAppointmentDetailsDto(appointment);
                return new ResponseEntity<>(appointmentDetailsDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AppointmentNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
     * Actors: Manager, Staff
     * */

    @GetMapping()
    public ResponseEntity<?> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            List<AppointmentForListDto> appointmentDtoList = appointments.stream().map(AppointmentMapper.INSTANCE::convertedToAppointmentDtoForList).collect(Collectors.toList());
            return new ResponseEntity<>(appointmentDtoList, HttpStatus.OK);
        } catch (AppointmentNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/customer")
    public ResponseEntity<?> getAppointments() {
        try {
            User customer = userService.getCustomerById(authenticationService.getAuthenticatedUserId()); // Check whether customer existed
            List<Appointment> appointments = appointmentService.getAppointmentsByCustomerId(customer.getUserId());
            List<AppointmentForListDto> appointmentForListDtos = appointments.stream().map(AppointmentMapper.INSTANCE::convertedToAppointmentDtoForList).collect(Collectors.toList());
            return new ResponseEntity<>(appointmentForListDtos, HttpStatus.OK);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // for Customer
    @PutMapping("/{appointmentId}")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer appointmentId, @RequestBody AppointmentUpdateDto appointmentUpdateDto) {

        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(appointmentUpdateDto, appointmentId);
            AppointmentDetailsDto appointmentDetailsDto = AppointmentMapper.INSTANCE.convertedToAppointmentDetailsDto(updatedAppointment);
            return new ResponseEntity<>(appointmentDetailsDto, HttpStatus.OK);
        } catch (AppointmentUpdatedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Integer appointmentId) {
        try {
            appointmentService.cancelAppointment(appointmentId);
            return new ResponseEntity<>("Canceling the appointment successfully", HttpStatus.OK);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // assign Vet for unassigned appointment
    // for Staff
    @PutMapping("/{appointmentId}/veterinarian/{veterinarianId}")
    public ResponseEntity<?> assignVeterinarianForAppointment(@PathVariable Integer appointmentId, @PathVariable Integer veterinarianId){
        try {
            appointmentService.assignVeterinarian(appointmentId, veterinarianId);
            return new ResponseEntity<>("Veterinarian assigned successfully", HttpStatus.OK);
        } catch (AppointmentNotFoundException | UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
