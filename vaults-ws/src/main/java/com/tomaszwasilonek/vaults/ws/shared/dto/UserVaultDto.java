package com.tomaszwasilonek.vaults.ws.shared.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserVaultDto implements Serializable {
	
	private static final long serialVersionUID = -3897005569956429345L;
	private long id;
	private String vaultId;
	private String name;
	private double balance;
}
