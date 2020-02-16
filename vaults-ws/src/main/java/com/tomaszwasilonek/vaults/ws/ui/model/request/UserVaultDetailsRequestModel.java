package com.tomaszwasilonek.vaults.ws.ui.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserVaultDetailsRequestModel {
	
	@NotBlank
	private String name;
	
	private double balance;

}
