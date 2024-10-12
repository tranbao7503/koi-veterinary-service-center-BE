package org.ftf.koifishveterinaryservicecenter.service.paymentservice;

import org.ftf.koifishveterinaryservicecenter.entity.Payment;

public interface PaymentService {
    Payment createPayment(Payment payment);

    Payment findPaymentByAppointmentId(Integer appointmentId);
}
