package org.ftf.koifishveterinaryservicecenter.exception;

public class VoucherQuantityExceededException extends RuntimeException {
    public VoucherQuantityExceededException(String message) {
        super(message);
    }
}
