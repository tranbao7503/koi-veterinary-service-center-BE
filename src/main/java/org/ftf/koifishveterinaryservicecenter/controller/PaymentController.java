package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.ApiResponse;
import org.ftf.koifishveterinaryservicecenter.dto.PaymentDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.PaymentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.PaymentMapper;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.paymentservice.PaymentService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final AppointmentService appointmentService;
    private final AuthenticationService authenticationService;

    @Autowired
    public PaymentController(
            PaymentService paymentService
            , AppointmentService appointmentService
            , AuthenticationService authenticationService) {
        this.paymentService = paymentService;
        this.appointmentService = appointmentService;
        this.authenticationService = authenticationService;
    }

    /*
     * Actors: Customer, Staff, Manager
     * */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getPaymentByAppointmentId(
            @PathVariable("appointmentId") Integer appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);

            if (authenticationService.getAuthenticatedUserRoleKey().equals("CUS")) { // Validate Customer
                if(!appointment.getCustomer().getUserId().equals(authenticationService.getAuthenticatedUserId())) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

            Payment payment = paymentService.findPaymentByAppointmentId(appointment.getAppointmentId());
            PaymentDto paymentDto = PaymentMapper.INSTANCE.convertToDto(payment);
            return new ResponseEntity<>(paymentDto, HttpStatus.OK);
        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PaymentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Actors: Staff
     * */
    @PutMapping("/{appointmentId}")
    public ResponseEntity<?> updatePayment(
            @PathVariable("appointmentId") Integer appointmentId
            , @RequestBody PaymentDto paymentDto) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);

            Payment payment = PaymentMapper.INSTANCE.convertToFullEntity(paymentDto);

            PaymentDto updatedPaymentDto = PaymentMapper.INSTANCE.convertToDto(paymentService.updatePayment(appointment.getPayment().getPaymentId(), payment));
            return new ResponseEntity<>(updatedPaymentDto, HttpStatus.OK);

        } catch (AppointmentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PaymentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
