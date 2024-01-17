package com.schol.sba.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.schol.sba.exception.AdminAlreadyExistException;
import com.schol.sba.exception.UserNotFoundByIdException;


@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{
	
	private ResponseEntity<Object> exceptionStructure(HttpStatus status,String message,Object rootCause) {
		return new ResponseEntity<Object>( Map.of(
				"status",status.value(),
				"message",message,
				"rootCause",rootCause
				),status); 
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<ObjectError> allErrors = ex.getAllErrors();
		Map<String, String> errors=new HashMap<String,String>(); 

		allErrors.forEach(error ->{
			FieldError fieldError=(FieldError) error; 
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());      
		});
		return exceptionStructure(HttpStatus.BAD_REQUEST, "Failed to save the data", errors);
	}
	
	
	@ExceptionHandler(UserNotFoundByIdException.class)  
	public ResponseEntity<Object> userNotFountById(UserNotFoundByIdException ex) {
		return exceptionStructure(HttpStatus.NOT_FOUND, ex.getMessage(), "User with this Id is not present!!");
	}
	
	@ExceptionHandler(AdminAlreadyExistException.class)  
	public ResponseEntity<Object> adminAlreadyExists(AdminAlreadyExistException ex) {
		return exceptionStructure(HttpStatus.NOT_FOUND, ex.getMessage(), "User with this Id is not present!!");
	}

}
