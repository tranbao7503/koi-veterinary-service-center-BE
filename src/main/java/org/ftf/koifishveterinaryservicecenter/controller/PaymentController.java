package org.ftf.koifishveterinaryservicecenter.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.ftf.koifishveterinaryservicecenter.configuration.PaymentConfig;
import org.ftf.koifishveterinaryservicecenter.dto.ApiResponse;
import org.ftf.koifishveterinaryservicecenter.dto.PaymentDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentMethod;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.PaymentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.PaymentMapper;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.paymentservice.PaymentService;
import org.ftf.koifishveterinaryservicecenter.service.paymentservice.VnPayService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final AppointmentService appointmentService;
    private final AuthenticationService authenticationService;
    private final VnPayService vnPayService;
    private final PaymentConfig paymentConfig;

    @Autowired
    public PaymentController(
            PaymentService paymentService
            , AppointmentService appointmentService
            , AuthenticationService authenticationService
            , VnPayService vnPayService, PaymentConfig paymentConfig) {
        this.paymentService = paymentService;
        this.appointmentService = appointmentService;
        this.authenticationService = authenticationService;
        this.vnPayService = vnPayService;
        this.paymentConfig = paymentConfig;
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
                if (!appointment.getCustomer().getUserId().equals(authenticationService.getAuthenticatedUserId())) {
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

    @GetMapping("/vnpay-link")
    public ResponseEntity<String> createPayment(
            @RequestParam("appointmentId") Integer appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            Payment payment = paymentService.findPaymentByAppointmentId(appointment.getAppointmentId());

            if (!authenticationService.getAuthenticatedUserId().equals(appointment.getCustomer().getUserId())) { // Invalid Customer
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (payment.getPaymentMethod().equals(PaymentMethod.CASH)) { // Reject Cash method
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (payment.getStatus().equals(PaymentStatus.PAID)) { // Reject PAID appointment
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String paymentUrl = vnPayService.createPaymentUrl(appointment);
            return new ResponseEntity<>(paymentUrl, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/vnpay-notify")
    public void handleVnPayNotify(
            @RequestParam Map<String, String> vnpParams
            , HttpServletResponse response) throws IOException {
        try {

            if (!vnPayService.verifySignature(vnpParams)) { // Verify Hash
                response.sendRedirect("http://localhost:8080/api/v1/payments/"); // Redirect to an error page on the FE
                return;
            }

            // Information after finish payment
            String txnRef = vnpParams.get("vnp_TxnRef");
            String transactionId = vnpParams.get("vnp_TransactionNo");
            String payDate = vnpParams.get("vnp_PayDate");
            String orderInfo = vnpParams.get("vnp_OrderInfo");
            String responseCode = vnpParams.get("vnp_ResponseCode");

            // Appointment ID
            Integer appointmentId = vnPayService.getAppointmentIdFromTxnRef(txnRef);

            if ("00".equals(responseCode)) { // Payed successfully

                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date paymentDate = dateFormat.parse(payDate);

                // Update payment
                paymentService.updatePaymentForVnPay(appointmentId, paymentDate, transactionId, orderInfo);

                response.sendRedirect("http://localhost:8080/api/v1/payments/" + appointmentId); // Redirect to appointment details page of FE
            } else {
                response.sendRedirect("http://localhost:8080/api/v1/payments/" + appointmentId);
            }
        } catch (Exception e) {
            response.sendRedirect("http://localhost:8080/api/v1/payments/");
        }
    }
}
