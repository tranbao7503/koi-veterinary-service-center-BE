package org.ftf.koifishveterinaryservicecenter.service.paymentservice;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.PaymentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.PaymentRepository;
import org.ftf.koifishveterinaryservicecenter.service.appointmentservice.AppointmentService;
import org.ftf.koifishveterinaryservicecenter.service.emailservice.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class PaymentServiceImpl implements PaymentService {


    private final PaymentRepository paymentRepository;
    private final EmailService emailService;
    private final AppointmentService appointmentService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, EmailService emailService, AppointmentService appointmentService) {
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
        this.appointmentService = appointmentService;
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
    public Payment updatePayment(Integer paymentId, Payment newPayment) throws PaymentNotFoundException {
        Payment payment = findPaymentByAppointmentId(paymentId);

        payment.setTransactionTime(LocalDateTime.now());
        payment.setDescription(newPayment.getDescription());
        payment.setStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);

        return payment;
    }


    @Override
    @Transactional
    public Payment  updatePaymentForVnPay(Integer paymentId, Date payDate, String transactionId, String description) {
        Payment payment = findPaymentByAppointmentId(paymentId);

        payment.setTransactionId(transactionId);
        payment.setTransactionTime(LocalDateTime.ofInstant(payDate.toInstant(), ZoneId.systemDefault()));
        payment.setDescription(description);
        payment.setStatus(PaymentStatus.PAID);

        Appointment appointment = appointmentService.getAppointmentByPaymentId(paymentId);
        String customerEmail = appointment.getCustomer().getEmail();

        // Asynchronously send the email
        emailService.sendAppointmentBills(customerEmail, "Your Appointment");

        return paymentRepository.save(payment);
    }
}
