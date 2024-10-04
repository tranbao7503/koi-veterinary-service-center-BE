package org.ftf.koifishveterinaryservicecenter.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    // Xóa hoặc sửa tên của constructor thứ hai
    // public UserServiceNotFoundException(String message) {
    //     super(message);
    // }
}
