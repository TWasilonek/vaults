package com.tomaszwasilonek.vaults.ws.ui.model.response;

import lombok.Data;

@Data
public class UserVaultsRest {
	
	private String vaultId;
	private String name;
	private double balance;
}
