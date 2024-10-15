package org.ftf.koifishveterinaryservicecenter.exception;

public class AppointmentServiceNotFoundException extends RuntimeException {

    public AppointmentServiceNotFoundException() {
    }

    public AppointmentServiceNotFoundException(String message) {
        super(message);
    }
}
