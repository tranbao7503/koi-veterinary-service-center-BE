package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Payment;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentMethod;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT p FROM Payment p JOIN Appointment a ON p.paymentId = a.payment.paymentId WHERE a.appointmentId = :appointmentId")
    Payment findByAppointmentId(Integer appointmentId);

    @Query("SELECT SUM(p.amount) FROM Payment p")
    Double sumTotalAmount();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE DATE(p.transactionTime) = CURRENT_DATE")
    Double sumTotalAmountToday();

    @Query("SELECT COUNT(p) FROM Payment p WHERE DATE(p.transactionTime) = CURRENT_DATE")
    long countPaymentsToday();

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentMethod = :paymentMethod")
    long countByPaymentMethod(@Param("paymentMethod") PaymentMethod paymentMethod);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentMethod = :paymentMethod AND DATE(p.transactionTime) = CURRENT_DATE")
    long countByPaymentMethodToday(@Param("paymentMethod") PaymentMethod paymentMethod);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    long countByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status AND DATE(p.transactionTime) = CURRENT_DATE")
    long countByStatusToday(@Param("status") PaymentStatus status);
  }