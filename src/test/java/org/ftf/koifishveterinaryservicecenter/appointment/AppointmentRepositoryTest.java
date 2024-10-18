package org.ftf.koifishveterinaryservicecenter.appointment;

import org.assertj.core.api.Assertions;
import org.ftf.koifishveterinaryservicecenter.entity.*;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentMethod;
import org.ftf.koifishveterinaryservicecenter.repository.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MedicalReportRepository medicalReportRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private PaymentRepository paymentRepository;



    @Disabled
    @Test
    public void testCreateMedicalReportSuccess() {
        int prescriptionId = 82;
        Optional<Prescription> prescription = prescriptionRepository.findById(prescriptionId);

        MedicalReport medicalReport = new MedicalReport();
        medicalReport.setConclusion("Pond temperature fluctuation noted.");
        medicalReport.setAdvise("Install heaters for temperature control.");

        medicalReport.setPrescription(prescription.get());

        User veterinarian = userRepository.findVeterinarianById(3);
        medicalReport.setVeterinarian(veterinarian);

        medicalReportRepository.save(medicalReport);
    }

    @Test
    public void testCreateAppointmentSuccess() {
        LocalDateTime date = LocalDateTime.now();
        Optional<Service> service = serviceRepository.findById(1);
        Address address = Address.builder().city("Ho Chi Minh").district("Quan 1").ward("Phuong Ben Nghe").homeNumber("88").build();
        TimeSlot slot = TimeSlot.builder().slotOrder(1).day(10).month(10).year(2024).build();
        TimeSlot savedTimeSlot = timeSlotRepository.save(slot);

        User customer = userRepository.findUsersByUserId(6);
        User veterinarian = userRepository.findVeterinarianById(11);
        String email = "customer_demo@gmail.com";
        String phone = "0987654321";
        String customerName = "My name is Demo";
        String description = "My description";
        BigDecimal totalPrice = service.get().getServicePrice();

        Payment payment = new Payment();
        payment.setAmount(totalPrice);
        payment.setPaymentMethod(PaymentMethod.CASH);

        Payment savedPayment = paymentRepository.save(payment);

        Appointment newAppointment = new Appointment();
        newAppointment.setCreatedDate(date);
        newAppointment.setService(service.get());
        newAppointment.setTimeSlot(savedTimeSlot);
        newAppointment.setCustomer(customer);
        newAppointment.setVeterinarian(veterinarian);
        newAppointment.setEmail(email);
        newAppointment.setPhoneNumber(phone);
        newAppointment.setCurrentStatus(AppointmentStatus.PENDING);
        newAppointment.setCustomerName(customerName);
        newAppointment.setDescription(description);
        newAppointment.setTotalPrice(totalPrice);
        newAppointment.setPayment(savedPayment);


        Appointment fromDb = appointmentRepository.save(newAppointment);

        Assertions.assertThat(fromDb).isNotNull();
    }

    @Test
    public void testGetAppointmentById() {
        Integer appointmentId = 23;
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        Assertions.assertThat(appointment).isPresent();
        System.out.println(appointment.get().getTimeSlot().toString());
    }

    @Test
    public void testUpdateAppointmentStatusTriggerInsertingToStatusTableSuccess() {
        Integer appointmentId = 26;

        // get appointment by Id
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        System.out.println("Before updating: " + appointment.get().getCurrentStatus());


        // update statues PENDING --> CONFIRMED
        appointment.get().setCurrentStatus(AppointmentStatus.CONFIRMED);

        // insert to Status table
        Status status = new Status();
        status.setAppointment(appointment.get());
        status.setStatusName(AppointmentStatus.CONFIRMED);
        status.setTime(LocalDateTime.now());
        status.setNote("Update to CONFIRMED successfully");
        appointment.get().addStatus(status);

        appointmentRepository.save(appointment.get());
        System.out.println("After updating: " + appointment.get().getCurrentStatus());

    }
}
