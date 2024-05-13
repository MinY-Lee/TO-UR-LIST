package com.eminyidle.tour.exception;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException(){

    }
    public InvalidUserNameException(String s){
        super(s);
    }
}
