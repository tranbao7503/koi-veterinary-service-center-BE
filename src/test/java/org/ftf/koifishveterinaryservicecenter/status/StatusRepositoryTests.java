package org.ftf.koifishveterinaryservicecenter.status;

import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.Status;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.repository.AppointmentRepository;
import org.ftf.koifishveterinaryservicecenter.repository.StatusRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class StatusRepositoryTests {
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    public void testInsertPaidStatusSuccess() {

        Integer appointmentId = 21;
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

        Status status = new Status();

        if (appointment.isPresent()) {
            User customer = appointment.get().getCustomer();

            status.setStatusName(String.valueOf(PaymentStatus.PAID));
            status.setTime(LocalDateTime.now());
            status.setNote("Customer - " + customer.getFirstName() + " " + customer.getLastName() + " " + PaymentStatus.PAID + " successfully");
            status.setAppointment(appointment.get());
            status.setUser(customer);
        }
        statusRepository.save(status);
    }
}
