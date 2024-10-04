package org.ftf.koifishveterinaryservicecenter.exception;

public class MedicineNotFoundException extends RuntimeException {

    public MedicineNotFoundException() {
    }

    public MedicineNotFoundException(String message) {
        super(message);
    }
}
