package org.ftf.koifishveterinaryservicecenter.service.paymentservice;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.PaymentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.repository.PaymentRepository;
import org.ftf.koifishveterinaryservicecenter.repository.StatusRepository;
import org.ftf.koifishveterinaryservicecenter.service.statusservice.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final StatusService statusService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, AppointmentRepository appointmentRepository, StatusService statusService) {
        this.paymentRepository = paymentRepository;
        this.statusService = statusService;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }


    @Override
    public Payment findPaymentByAppointmentId(Integer appointmentId) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId);
        if (payment == null) {
            throw new PaymentNotFoundException("Payment not found with appointment: " + appointmentId);
        }
        return payment;
    }

    @Override
    @Transactional
    public Payment confirmPayment(Integer paymentId, Payment newPayment) throws PaymentNotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));

        payment.setTransactionTime(LocalDateTime.now());
        payment.setDescription(newPayment.getDescription());
        payment.setStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);

        // Log in status
        Appointment appointment = appointmentRepository.findAppointmentByPaymentId(paymentId);
        User customer = appointment.getCustomer();
        statusService.customerLogStatusPayment(appointment, customer, payment.getStatus());

        return payment;
    }


    @Override
    @Transactional
    public Payment updatePaymentForVnPay(Integer appointmentId, Date payDate, String transactionId, String description) {
        Payment payment = findPaymentByAppointmentId(appointmentId);
        payment.setTransactionId(transactionId);
        payment.setTransactionTime(LocalDateTime.ofInstant(payDate.toInstant(), ZoneId.systemDefault()));
        payment.setDescription(URLDecoder.decode(description));
        payment.setStatus(PaymentStatus.PAID);

        // Log in status
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            User customer = appointment.get().getCustomer();
            statusService.customerLogStatusPayment(appointment.get(), customer, payment.getStatus());
        }

        return paymentRepository.save(payment);
    }

    @Override
    public Payment refundPayment(Integer paymentId, Payment newPayment) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));

        payment.setTransactionTime(LocalDateTime.now());
        payment.setPaymentMethod(newPayment.getPaymentMethod());
        payment.setTransactionId(newPayment.getTransactionId());
        payment.setDescription(newPayment.getDescription());
        payment.setAmount(newPayment.getAmount());
        payment.setStatus(PaymentStatus.REFUNDED);

        paymentRepository.save(payment);

        // Log in status
        Appointment appointment = appointmentRepository.findAppointmentByPaymentId(paymentId);
        User customer = appointment.getCustomer();
        statusService.customerLogStatusPayment(appointment, customer, payment.getStatus());


        return payment;
    }

    @Override
    public Payment findPaymentByPaymentId(Integer paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (payment.isEmpty())
            throw new PaymentNotFoundException("Payment not found with id: " + paymentId);
        return payment.get();
    }

}


