/**
 * Custom exceptions handler. Enable methods if you want to handle some exceptions differently
 */

package com.tomaszwasilonek.vaults.ws.exceptions;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.tomaszwasilonek.vaults.ws.ui.model.response.ApiError;

@ControllerAdvice
public class RestExceptionsHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	 protected ResponseEntity<Object> handleEntityNotFound(
	           EntityNotFoundException ex) {
		
   	ApiError apiError = new ApiError(NOT_FOUND);
       apiError.setMessage(ex.getMessage());
       
       return buildResponseEntity(apiError);
   }
	
	
	/*
	 * Util method to create a Response Entity
	 */
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	
	
	// custom handler for UserServiceException
//	@ExceptionHandler(value = {UserServiceException.class})
//	public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request){
//		
//		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
//		
//		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	
	// custom handler for all other exceptions
//	@ExceptionHandler(value = {Exception.class})
//	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request){
//		
//		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
//		
//		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//	}

}
