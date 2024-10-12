package org.ftf.koifishveterinaryservicecenter.service.appointmentservice;

import org.ftf.koifishveterinaryservicecenter.entity.*;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.exception.AppointmentServiceNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.MedicalReportNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.StatusNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.*;
import org.ftf.koifishveterinaryservicecenter.service.medicalreportservice.MedicalReportService;
import org.ftf.koifishveterinaryservicecenter.service.paymentservice.PaymentService;
import org.ftf.koifishveterinaryservicecenter.service.serviceservice.ServiceService;
import org.ftf.koifishveterinaryservicecenter.service.slotservice.SlotService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalReportService medicalReportService;
    private final UserService userService;
    private final MedicalReportRepository medicalReportRepository;
    private final ServiceService serviceService;
    private final SlotService slotService;
    private final PaymentService paymentService;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository
            , MedicalReportService medicalReportService
            , UserService userService
            , MedicalReportRepository medicalReportRepository
            , ServiceService serviceService
            , SlotService slotService, PaymentService paymentService) {
        this.appointmentRepository = appointmentRepository;
        this.medicalReportService = medicalReportService;
        this.userService = userService;
        this.medicalReportRepository = medicalReportRepository;
        this.serviceService = serviceService;
        this.slotService = slotService;
        this.paymentService = paymentService;
    }


    @Override
    public void createMedicalReport(MedicalReport medicalReport, Integer appointmentId, Integer prescriptionId, Integer veterinarianId) {

        // get prescription on db by id
        Prescription prescription = medicalReportService.findPrescriptionById(prescriptionId);

        // set prop for medicalReport
        medicalReport.setPrescription(prescription);

        User veterinarian = userService.getVeterinarianById(veterinarianId);
        medicalReport.setVeterinarian(veterinarian);

        MedicalReport savedMedicalReport = medicalReportRepository.save(medicalReport);

        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setMedicalReport(savedMedicalReport);

        appointmentRepository.save(appointment);
    }

    @Override
    public List<Status> findStatusByAppointmentId(Integer appointmentId) throws AppointmentServiceNotFoundException {

        // Get appointment by Appointment Id - Not found => exception
        Appointment appointment = this.getAppointmentById(appointmentId);

        // Get status list
        List<Status> statuses = new ArrayList<>(appointment.getStatuses());
        if (statuses.isEmpty()) {
            throw new StatusNotFoundException("Not found status logs of Appointment with id: " + appointmentId);
        }

        // Sort status by Id
        statuses.sort(Comparator.comparing(Status::getStatusId));

        return statuses;
    }


    public void createAppointment(Appointment appointment, Integer customerId) {
        // 1. online booking

        // setting fields for newAppointment
        Appointment newAppointment = new Appointment();

        // create_date
        newAppointment.setCreatedDate(LocalDateTime.now());

        // service_id
        org.ftf.koifishveterinaryservicecenter.entity.Service bookedService = serviceService.getServiceById(appointment.getService().getServiceId());
        newAppointment.setService(bookedService);

        // address_id
        // moving_surcharge_id

        // slot_id
        TimeSlot timeSlot = slotService.getTimeSlotById(appointment.getTimeSlot().getSlotId());
        newAppointment.setTimeSlot(timeSlot);

        // feedback_id
        // report_id

        // user_id
        User userFromDb = userService.getCustomerById(customerId);
        newAppointment.setCustomer(userFromDb);

        // veterinarian_id
        if (appointment.getVeterinarian() != null) {
            User veterinarianFromDb = userService.getVeterinarianById(appointment.getVeterinarian().getUserId());
            newAppointment.setVeterinarian(veterinarianFromDb);
        }

        // email
        newAppointment.setEmail(appointment.getEmail());

        // phone
        newAppointment.setPhoneNumber(appointment.getPhoneNumber());

        // current_status : **
        newAppointment.setCurrentStatus(AppointmentStatus.PENDING);

        // customer_name
        newAppointment.setCustomerName(appointment.getCustomerName());

        // description
        newAppointment.setDescription(appointment.getDescription());

        // total price
        newAppointment.setTotalPrice(calculatePrice(newAppointment));

        // payment_id
        Payment payment = appointment.getPayment();
        payment.setAmount(newAppointment.getTotalPrice());
        Payment savedPayment = paymentService.createPayment(payment);
        newAppointment.setPayment(savedPayment);

        appointmentRepository.save(newAppointment);
    }

    @Override
    public List<Appointment> getAppointmentsByCustomerId(Integer customerId) {
        List<Appointment> appointments = appointmentRepository.findAppointmentByCustomerId(customerId);
        if(appointments.isEmpty()) {
            throw  new AppointmentServiceNotFoundException("Appointment not found!");
        }
        // Sort by newest appointment
        appointments.sort(Comparator.comparing(Appointment::getAppointmentId).reversed());

        return appointments;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if(appointments.isEmpty()) {
            throw new AppointmentServiceNotFoundException("Not found appointments");
        }
        // Sort by created date
        appointments.sort(Comparator.comparing(Appointment::getAppointmentId).reversed());

        return appointments;
    }

    @Override
    public Appointment getAppointmentById(Integer appointmentId) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isEmpty())
            throw new AppointmentServiceNotFoundException("Not found Appointment with Id: " + appointmentId);
        return appointmentOptional.get();
    }

    @Override
    public MedicalReport getMedicalReportByAppointmentId(Integer appointmentId) throws AppointmentServiceNotFoundException {
        Appointment appointment = getAppointmentById(appointmentId);
        MedicalReport medicalReport = medicalReportRepository.findByReportId(appointment.getMedicalReport().getReportId());
        if(medicalReport == null){
            throw new MedicalReportNotFoundException("Not found Medical Report with appointment id: " + appointmentId);
        }
        return medicalReport;
    }

    private BigDecimal calculatePrice(Appointment appointment) {
        BigDecimal servicePrice = appointment.getService().getServicePrice();
        return appointment.getMovingSurcharge() == null ? servicePrice : servicePrice.add(appointment.getMovingSurcharge().getPrice());
    }

}
