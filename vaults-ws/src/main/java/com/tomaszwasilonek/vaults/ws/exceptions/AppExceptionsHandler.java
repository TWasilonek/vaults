/**
 * Custom exceptions handler. Enable methods if you want to handle some exceptions differently
 */

package com.tomaszwasilonek.vaults.ws.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.tomaszwasilonek.vaults.ws.ui.model.response.ErrorMessage;

@ControllerAdvice
public class AppExceptionsHandler {
	
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
