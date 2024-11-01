package org.ftf.koifishveterinaryservicecenter.service.paymentservice;

import org.ftf.koifishveterinaryservicecenter.entity.Payment;

import java.util.Date;

public interface PaymentService {
    Payment createPayment(Payment payment);

    Payment findPaymentByAppointmentId(Integer appointmentId);

    Payment confirmPayment(Integer paymentId, Payment payment);

    Payment updatePaymentForVnPay(Integer appointmentId, Date payDate, String transactionId, String description);

    Payment refundPayment(Integer paymentId, Payment newPayment);
}
