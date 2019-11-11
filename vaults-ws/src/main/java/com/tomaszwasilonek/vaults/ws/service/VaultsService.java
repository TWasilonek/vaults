package com.tomaszwasilonek.vaults.ws.service;

import java.util.List;

import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.shared.dto.VaultsDto;

public interface VaultsService {
	VaultsDto createVault(UserEntity user, VaultsDto vault);
	
	List<VaultsDto> getVaults(UserEntity user);
	
	VaultsDto getVault(String vaultId);
	
	VaultsDto updateVault(String vaultId, VaultsDto vaultDetails);
	
	void deleteVault(String vaultId);
}
