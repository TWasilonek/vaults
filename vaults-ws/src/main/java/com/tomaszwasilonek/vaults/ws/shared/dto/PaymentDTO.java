package com.tomaszwasilonek.vaults.ws.shared.dto;

import com.tomaszwasilonek.vaults.ws.shared.constants.ApplicationConstants;

import lombok.Data;

@Data
public class PaymentDTO {
	private double amount;
	private String sourceAccount;
	private String sourceSubject;
	private String destinationAccount;
	private String destinationSubject;
}
