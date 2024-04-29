package com.eminyidle.tour.exhandler;

import com.eminyidle.tour.exception.AbnormalTourDateException;
import com.eminyidle.tour.exception.NoHostPrivilegesException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class TourExceptionHandler {

    @ExceptionHandler(NoHostPrivilegesException.class)
    public ResponseEntity<String> handleNoHostPrivilegesException(Exception e){
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(AbnormalTourDateException.class)
    public ResponseEntity<String> handleAbnormalTourDateException(Exception e){
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }
}
