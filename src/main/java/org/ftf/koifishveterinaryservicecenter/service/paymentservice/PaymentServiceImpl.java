package org.ftf.koifishveterinaryservicecenter.service.paymentservice;

import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.PaymentNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class PaymentServiceImpl implements PaymentService {


    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
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
    public Payment updatePayment(Integer paymentId, Payment newPayment) throws PaymentNotFoundException {
        Payment payment = findPaymentByAppointmentId(paymentId);

        payment.setTransactionTime(LocalDateTime.now());
        payment.setDescription(newPayment.getDescription());
        payment.setStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);

        return payment;
    }

    @Override
    public Payment updatePaymentForVnPay(Integer paymentId, Date payDate, String transactionId, String description) {
        Payment payment = findPaymentByAppointmentId(paymentId);

        payment.setTransactionId(transactionId);
        payment.setTransactionTime(LocalDateTime.ofInstant(payDate.toInstant(), ZoneId.systemDefault()));
        payment.setDescription(description);
        payment.setStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);

        return payment;
    }
}
