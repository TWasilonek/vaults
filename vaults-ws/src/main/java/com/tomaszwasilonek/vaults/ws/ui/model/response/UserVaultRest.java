package com.tomaszwasilonek.vaults.ws.ui.model.response;

import lombok.Data;

@Data
public class UserVaultRest {
	
	private String vaultId;
	private String name;
	private double balance;
}
