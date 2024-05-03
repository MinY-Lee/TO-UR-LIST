package com.eminyidle.tour.exhandler;

import com.eminyidle.tour.exception.AbnormalTourDateException;
import com.eminyidle.tour.exception.NoHostPrivilegesException;
import com.eminyidle.tour.exception.NoSuchTourException;
import com.eminyidle.tour.exception.UserInfoInRequestNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
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

    @ExceptionHandler(NoSuchTourException.class)
    public ResponseEntity<String> handleNoSuchTourException(Exception e){
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(UserInfoInRequestNotFoundException.class)
    public ResponseEntity<String> handleUserInfoInRequestNotFoundException(Exception e){
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    //FIXME - userID가 없는 헤더에만 대한 예외처리로 바꾸기
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingRequestHeaderException(Exception e){
        return ResponseEntity.badRequest()
                .body("No UserID");
    }

}
