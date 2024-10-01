package org.ftf.koifishveterinaryservicecenter.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Exception e) {
        super(message, e);
    }

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
