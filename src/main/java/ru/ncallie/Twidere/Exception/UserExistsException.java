package ru.ncallie.Twidere.Exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String msg) {
        super(msg);
    }
}
