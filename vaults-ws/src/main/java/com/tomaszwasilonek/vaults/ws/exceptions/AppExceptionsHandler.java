/**
 * Custom exceptions handler. Enable methods if you want to handle some exceptions differently
 */

package com.tomaszwasilonek.vaults.ws.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.tomaszwasilonek.vaults.ws.ui.model.response.ApiError;

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
