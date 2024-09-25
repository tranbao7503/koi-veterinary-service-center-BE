package org.ftf.koifishveterinaryservicecenter.koifishservice.exception;

public class AppointmentServiceNotFoundException extends RuntimeException {
    public AppointmentServiceNotFoundException() {
    }

    public AppointmentServiceNotFoundException(String message) {
        super(message);
    }
}
