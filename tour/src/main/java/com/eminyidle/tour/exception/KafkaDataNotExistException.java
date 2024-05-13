package com.eminyidle.tour.exception;

public class KafkaDataNotExistException extends RuntimeException {
    public KafkaDataNotExistException(String s) {
        super(s);
    }
    public KafkaDataNotExistException(){

    }
}
