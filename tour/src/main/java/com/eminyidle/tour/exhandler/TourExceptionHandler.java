package com.eminyidle.tour.exhandler;

import com.eminyidle.tour.exception.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
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
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(AbnormalTourDateException.class)
    public ResponseEntity<String> handleAbnormalTourDateException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("올바른 여행날짜가 아닙니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(NoSuchTourException.class)
    public ResponseEntity<String> handleNoSuchTourException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("요청한 투어가 없습니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<String> handleNoSuchUserException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("해당 유저가 없습니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(NoSuchMemberException.class)
    public ResponseEntity<String> handleNoSuchMemberException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("해당 유저는 멤버가 아닙니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(UserInfoInRequestNotFoundException.class)
    public ResponseEntity<String> handleUserInfoInRequestNotFoundException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("유저 정보가 없습니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    //FIXME - userID가 없는 헤더에만 대한 예외처리로 바꾸기
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingRequestHeaderException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("유저아이디가 없습니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }


    @ExceptionHandler(DuplicatedGhostNicknameException.class)
    public ResponseEntity<String> handleDuplicatedGhostNicknameException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("중복된 고스트 닉네임입니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(UndefinedMemberTypeException.class)
    public ResponseEntity<String> handleUndefinedMemberTypeException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("정의되지 않은 멤버 타입입니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(HostCanNotBeDeletedException.class)
    public ResponseEntity<String> handleHostCanNotBeDeletedException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("호스트는 삭제할 수 없습니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(AlreadyUserAttendTourException.class)
    public ResponseEntity<String> handleAlreadyUserAttendTourException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("이미 참여중인 사용자입니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }
    @ExceptionHandler(FailToGetUserRequestException.class)
    public ResponseEntity<String> handleFailToGetUserRequestException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("유저 정보를 불러올 수 없습니다");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    /**
     * Spring에서는 JPA의 예외를 자동으로 감지하여, Spring의 일반적인 데이터 접근 예외 계층인 DataAccessException 계층으로 변환한다.
     * DataAccessException은 더욱 범용적인 예외 처리를 가능하게 해 주며, JPA 예외를 포함한 다양한 데이터베이스 관련 예외를 포함한다.
     * @param e
     * @return
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("DB접근 중 오류가 발생했습니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(NoSuchCityException.class)
    public ResponseEntity<String> handleNoSuchCityException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("주어진 도시가 없습니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(InvalidTourTitleFormatException.class)
    public ResponseEntity<String> handleInvalidTourTitleFormatException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("여행 제목은 1~16자 사이 문자열이어야 합니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(InvalidTourDateException.class)
    public ResponseEntity<String> handleInvalidTourDateException(Exception e){
        StringBuilder errorMessage=new StringBuilder();
        makeErrorMessage(errorMessage,e);
        errorMessage.append("여행날짜 형식이 올바르지 않습니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }
}
