package com.eminyidle.checklist.exhandler;

import com.eminyidle.checklist.exception.CreateItemException;
import com.eminyidle.checklist.exception.UserNotInTourException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChecklistExceptionHandler {

    @ExceptionHandler(UserNotInTourException.class)
    public ResponseEntity<String> handleUserNotInTourException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CreateItemException.class)
    public ResponseEntity<String> handleCreateItemException(Exception e){
        return ResponseEntity.badRequest().body("");
    }
}
