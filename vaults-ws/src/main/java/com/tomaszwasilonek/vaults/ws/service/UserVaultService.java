package com.tomaszwasilonek.vaults.ws.service;

import java.util.List;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;

public interface UserVaultService {
	UserVaultDto createVault(UserEntity user, UserVaultDto vault);
	
	List<UserVaultDto> getVaults(UserEntity user);
	
	UserVaultDto getVault(String vaultId);
	
	UserVaultDto updateVault(String vaultId, UserVaultDto vaultDetails);
	
	void deleteVault(String vaultId);
}
