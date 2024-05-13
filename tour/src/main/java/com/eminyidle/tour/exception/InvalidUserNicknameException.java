package com.eminyidle.tour.exception;

public class InvalidUserNicknameException extends RuntimeException {
    public InvalidUserNicknameException() {
    }

    public InvalidUserNicknameException(String s) {
        super(s);
    }
}
