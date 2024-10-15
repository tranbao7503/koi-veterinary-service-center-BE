package org.ftf.koifishveterinaryservicecenter.exception;

public class FishNotFoundException extends RuntimeException {
    public FishNotFoundException() {
    }

    public FishNotFoundException(String message) {
        super(message);
    }
}
