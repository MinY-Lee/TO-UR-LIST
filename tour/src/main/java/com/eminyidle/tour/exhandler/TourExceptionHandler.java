package com.eminyidle.tour.exhandler;

import com.eminyidle.tour.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TourExceptionHandler {
    private void makeErrorMessage(StringBuilder errorMessage, Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        if (stackTrace.length > 0) {
            StackTraceElement topFrame = stackTrace[0];
            String className = topFrame.getClassName();
            String methodName = topFrame.getMethodName();

            errorMessage.append(className).append(".").append(methodName).append(": ");
        }
    }

    @ExceptionHandler(NoHostPrivilegesException.class)
    public ResponseEntity<String> handleNoHostPrivilegesException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("호스트 권한이 없습니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }

    @ExceptionHandler(AbnormalTourDateException.class)
    public ResponseEntity<String> handleAbnormalTourDateException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("올바른 여행날짜가 아닙니다.");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }

    @ExceptionHandler(NoSuchTourException.class)
    public ResponseEntity<String> handleNoSuchTourException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("요청한 투어가 없습니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }

    @ExceptionHandler(UserInfoInRequestNotFoundException.class)
    public ResponseEntity<String> handleUserInfoInRequestNotFoundException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("유저 정보가 없습니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }

    //FIXME - userID가 없는 헤더에만 대한 예외처리로 바꾸기
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingRequestHeaderException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("유저아이디가 없습니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }


    @ExceptionHandler(DuplicatedGhostNicknameException.class)
    public ResponseEntity<String> handleDuplicatedGhostNicknameException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("중복된 고스트 닉네임입니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }

    @ExceptionHandler(InvalidMemberTypeException.class)
    public ResponseEntity<String> handleInvalidMemberTypeException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("올바른 멤버 타입이 아닙니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }

    @ExceptionHandler(HostCanNotBeDeletedException.class)
    public ResponseEntity<String> handleHostCanNotBeDeletedException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("호스트는 삭제할 수 없습니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }

    @ExceptionHandler(AlreadyUserAttendTourException.class)
    public ResponseEntity<String> handleAlreadyUserAttendTourException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("이미 참여중인 사용자입니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }
    @ExceptionHandler(FailToGetUserRequestException.class)
    public ResponseEntity<String> handleFailToGetUserRequestException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("유저 정보를 불러올 수 없습니다");
        return ResponseEntity.badRequest()
                .body(errorMessage.toString());
    }
}
