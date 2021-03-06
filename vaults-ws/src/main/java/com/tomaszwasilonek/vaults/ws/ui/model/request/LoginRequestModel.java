package com.tomaszwasilonek.vaults.ws.ui.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestModel {
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Min(value=4)
	private String password;

}
