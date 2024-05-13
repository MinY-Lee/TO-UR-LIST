package com.eminyidle.checklist.exception;

public class KafkaDataNotExistException extends RuntimeException {
    public KafkaDataNotExistException(String s) {
        super(s);
    }
}
