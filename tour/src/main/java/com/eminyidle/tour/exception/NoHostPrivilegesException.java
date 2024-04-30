package com.eminyidle.tour.exception;

public class NoHostPrivilegesException extends RuntimeException {
    @Override
    public String getMessage() {
        return "호스트 권한이 없습니다.\n";
    }
}
