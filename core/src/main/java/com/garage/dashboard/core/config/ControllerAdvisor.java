package com.garage.dashboard.core.config;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.garage.dashboard.core.exceptions.BusinessException;
import com.garage.dashboard.core.exceptions.NotFoundException;
import com.garage.dashboard.core.exceptions.UnauthorizedException;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
@RestController
@Validated
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvisor.class);

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBussinessException(BusinessException ex) {
		LOGGER.error("{}", ex.getMessage());
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
		LOGGER.error("{}", ex.getMessage());
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNAUTHORIZED.value());

		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
		LOGGER.error("{}", ex.getMessage());
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(URISyntaxException.class)
	public ResponseEntity<?> handleURISyntaxException(URISyntaxException ex) {
		LOGGER.error("{}", ex.getMessage());
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
		LOGGER.error("{}", ex.getMessage());
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleConstraintViolationException(IllegalArgumentException ex) {
		LOGGER.error("{}", ex.getMessage());
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
		LOGGER.error("{}", ex.getMessage());
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		List<String> errorList = new ArrayList<String>();

		LOGGER.error("{}", ex.getMessage());

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			StringBuilder errors = new StringBuilder();
			errors.append(fieldName);
			errors.append(" ");
			errors.append(errorMessage);
			errorList.add(errors.toString());
		});
		body.put("timestamp", LocalDateTime.now());
		body.put("message", errorList);
		body.put("status", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
	}

}
