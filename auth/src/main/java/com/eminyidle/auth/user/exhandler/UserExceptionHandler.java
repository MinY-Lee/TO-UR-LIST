package com.eminyidle.auth.user.exhandler;

import com.eminyidle.auth.user.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.eminyidle.auth.user.controller")
public class UserExceptionHandler {

    private void makeErrorMessage(StringBuilder errorMessage, Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        if (stackTrace.length > 0) {
            StackTraceElement topFrame = stackTrace[0];
            String className = topFrame.getClassName();
            String methodName = topFrame.getMethodName();

            errorMessage.append(className).append(".").append(methodName).append(": ");
        }
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> emailAlreadyExistsExceptionHandler(
            EmailAlreadyExistsException e) {
        StringBuilder errorMessage = new StringBuilder();

        makeErrorMessage(errorMessage, e);

        errorMessage.append("이미 존재하는 이메일입니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(NicknameNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> nicknameNullExceptionHandler(NicknameNullException e) {
        StringBuilder errorMessage = new StringBuilder();

        makeErrorMessage(errorMessage, e);

        errorMessage.append("이름을 입력하지 않았습니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(UserNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> userNotExistExceptionHandler(UserNotExistException e) {
        StringBuilder errorMessage = new StringBuilder();

        makeErrorMessage(errorMessage, e);

        errorMessage.append("존재하지 않는 사용자입니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(GameInfoNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> gameInfoNullExceptionHandler(GameInfoNullException e) {
        StringBuilder errorMessage = new StringBuilder();

        makeErrorMessage(errorMessage, e);

        errorMessage.append("게임 정보가 없습니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

    @ExceptionHandler(TitleNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<String> titleNotExistExceptionHandler(TitleNotExistException e) {
        StringBuilder errorMessage = new StringBuilder();

        makeErrorMessage(errorMessage, e);

        errorMessage.append("존재하지 않는 칭호입니다.");
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }

}
