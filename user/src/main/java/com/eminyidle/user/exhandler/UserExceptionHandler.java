package com.eminyidle.user.exhandler;

import com.eminyidle.user.exception.InvalidGenderException;
import com.eminyidle.user.exception.InvalidUserNicknameException;
import com.eminyidle.user.exception.InvalidUsernameException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
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

	@ExceptionHandler(InvalidGenderException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<String> invalidGenderExceptionHandler(InvalidGenderException e) {
		StringBuilder errorMessage = new StringBuilder();

		makeErrorMessage(errorMessage, e);

		errorMessage.append(e.getMessage());
		log.error(errorMessage.toString());
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(InvalidUsernameException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<String> invalidUsernameExceptionHandler(InvalidUsernameException e) {
		StringBuilder errorMessage = new StringBuilder();

		makeErrorMessage(errorMessage, e);

		errorMessage.append(e.getMessage());
		log.error(errorMessage.toString());
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(InvalidUserNicknameException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<String> invalidUserNicknameExceptionHandler(InvalidUserNicknameException e) {
		StringBuilder errorMessage = new StringBuilder();

		makeErrorMessage(errorMessage, e);

		errorMessage.append(e.getMessage());
		log.error(errorMessage.toString());
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<String> invalidUserNicknameExceptionHandler(NoSuchElementException e) {
		StringBuilder errorMessage = new StringBuilder();

		makeErrorMessage(errorMessage, e);

		log.error(errorMessage.toString());
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<String> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
		StringBuilder errorMessage = new StringBuilder();

		makeErrorMessage(errorMessage, e);

		log.error(errorMessage.toString());
		return ResponseEntity.badRequest().body(e.getMessage());
	}

}
