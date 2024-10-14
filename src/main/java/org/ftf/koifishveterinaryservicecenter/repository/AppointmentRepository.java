package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentDto;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("SELECT a FROM Appointment a WHERE a.customer.userId = :customerId ")
    List<Appointment> findAppointmentByCustomerId(Integer customerId);


}