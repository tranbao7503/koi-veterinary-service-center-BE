package org.ftf.koifishveterinaryservicecenter.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException() {
    }

    public AddressNotFoundException(String message) {
        super(message);
    }
}
