package com.tomaszwasilonek.vaults.ws.ui.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyTransferRequestModel {

	@NotNull(message = "amount is required")
	private double amount;
	
	@NotBlank(message = "source vault id is required")
	private String sourceAccount;
	
	@NotBlank(message = "destination vault id is required")
	private String destinationAccount;
	
	private String description;
}
