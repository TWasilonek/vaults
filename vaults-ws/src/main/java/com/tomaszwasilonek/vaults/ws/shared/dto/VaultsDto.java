package com.tomaszwasilonek.vaults.ws.shared.dto;

import java.io.Serializable;

public class VaultsDto implements Serializable {
	private static final long serialVersionUID = -3897005569956429345L;
	private long id;
	private String vaultId;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVaultId() {
		return vaultId;
	}

	public void setVaultId(String vaultId) {
		this.vaultId = vaultId;
	}
	
	
}
