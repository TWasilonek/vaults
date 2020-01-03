package com.tomaszwasilonek.vaults.ws.service;

import java.util.List;

import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultsDto;

public interface VaultsService {
	UserVaultsDto createVault(UserEntity user, UserVaultsDto vault);
	
	List<UserVaultsDto> getVaults(UserEntity user);
	
	UserVaultsDto getVault(String vaultId);
	
	UserVaultsDto updateVault(String vaultId, UserVaultsDto vaultDetails);
	
	void deleteVault(String vaultId);
}
