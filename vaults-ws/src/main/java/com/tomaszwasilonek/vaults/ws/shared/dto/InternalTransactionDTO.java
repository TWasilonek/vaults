package com.tomaszwasilonek.vaults.ws.shared.dto;

import lombok.Data;

@Data	
public class InternalTransactionDTO {
	private double amount;
	private String sourceVaultId;
	private String targetVaultId;
}
