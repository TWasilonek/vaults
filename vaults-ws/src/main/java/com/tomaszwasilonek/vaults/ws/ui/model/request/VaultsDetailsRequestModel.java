package com.tomaszwasilonek.vaults.ws.ui.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class VaultsDetailsRequestModel {
	
	@NotBlank
	private String name;
	
	private double balance;

}
