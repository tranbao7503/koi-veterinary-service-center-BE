package org.ftf.koifishveterinaryservicecenter.exception;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException() {
    }

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
