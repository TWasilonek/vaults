package com.tomaszwasilonek.vaults.ws.ui.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequestModel {
	
	@NotNull(message = "amount is required")
	private double amount;
	
	@NotBlank(message = "source account is required")
	private String sourceAccount;
	
	@NotBlank(message = "source subject is required")
	private String sourceSubject;
	
	@NotBlank(message = "destination subject is required")
	private String destinationAccount;
	
	private String description;
}
