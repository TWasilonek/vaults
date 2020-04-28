package com.tomaszwasilonek.vaults.ws.shared.dto;

import com.tomaszwasilonek.vaults.ws.shared.constants.ApplicationConstants;

import lombok.Data;

@Data
public class PaymentDTO {
	public final String DESTINATION_SUBJECT = ApplicationConstants.APP_NAME;
	
	private double amount;
	private String sourceAccount;
	private String sourceSubject;
	private String destinationAccount;
}
