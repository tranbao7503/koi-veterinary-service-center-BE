package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("SELECT a FROM Appointment a WHERE a.customer.userId = :customerId ")
    List<Appointment> findAppointmentByCustomerId(Integer customerId);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE DATE(a.createdDate) = CURRENT_DATE")
    long countAppointmentsToday();

    long countByService_ServiceId(long serviceId);


    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.service.serviceId = :serviceId AND DATE(a.createdDate) = CURRENT_DATE")
    long countByService_ServiceIdToday(@Param("serviceId") long serviceId);



}



