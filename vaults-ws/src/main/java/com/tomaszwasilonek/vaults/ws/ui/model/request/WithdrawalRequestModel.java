package com.tomaszwasilonek.vaults.ws.ui.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawalRequestModel {
	
	@NotNull(message = "amount is required")
	private double amount;
	
	@NotBlank(message = "source account is required")
	private String sourceAccount;
	
	@NotBlank(message = "destination account is required")
	private String destinationSubject;
	
	@NotBlank(message = "destination subject is required")
	private String destinationAccount;
	
	private String description;
}
