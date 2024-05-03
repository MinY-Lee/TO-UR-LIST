package com.eminyidle.tour.exhandler;

import com.eminyidle.tour.exception.*;
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
    
    @ExceptionHandler(DuplicatedGhostNicknameException.class)
    public ResponseEntity<String> handleDuplicatedGhostNicknameException(Exception e){
        return ResponseEntity.badRequest()
                .body("중복된 고스트 닉네임입니다");
    }

    @ExceptionHandler(InvalidMemberTypeException.class)
    public ResponseEntity<String> handleInvalidMemberTypeException(Exception e){
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(HostCanNotBeDeletedException.class)
    public ResponseEntity<String> handleHostCanNotBeDeletedException(Exception e){
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }
}
