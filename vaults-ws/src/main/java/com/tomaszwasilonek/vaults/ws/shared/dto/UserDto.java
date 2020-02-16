package com.tomaszwasilonek.vaults.ws.shared.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDto implements Serializable {
	
	private static final long serialVersionUID = 8412333006980134940L;
	private long id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String encryptedPassword;
	private String emailVerificationToken;
	private Boolean emailVerificationStatus = false;
	
}
