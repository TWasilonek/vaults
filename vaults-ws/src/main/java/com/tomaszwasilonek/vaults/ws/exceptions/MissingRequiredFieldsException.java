package com.tomaszwasilonek.vaults.ws.exceptions;

public class MissingRequiredFieldsException extends RuntimeException {
	
	private static final long serialVersionUID = -3447398303777029455L;
	
	public MissingRequiredFieldsException(String message) {
		super(message);
	}

}
