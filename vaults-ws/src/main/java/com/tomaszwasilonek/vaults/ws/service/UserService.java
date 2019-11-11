package com.tomaszwasilonek.vaults.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.shared.dto.VaultsDto;

public interface UserService extends UserDetailsService {
	
	UserDto createUser(UserDto user);
	
	UserDto updateUser(String userId, UserDto user);
	
	UserDto getUser(String email);
	
	UserDto getUserByUserId(String userId);
	
	List<UserDto> getUsers(int page, int limit);
	
	void deleteUser(String userId);
	
	VaultsDto createVault(String userId, VaultsDto vault);
	
	List<VaultsDto> getVaults(String userId);
	
	VaultsDto getVaultByVaultId(String userId, String vaultId);
	
	VaultsDto updateVault(String userId, String vaultId, VaultsDto vaultDetails);
	
	void deleteVault(String userId, String vaultId);
}
