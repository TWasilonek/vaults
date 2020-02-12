package com.tomaszwasilonek.vaults.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;

public interface UserService extends UserDetailsService {
	
	UserDto createUser(UserDto user);
	
	UserDto updateUser(String userId, UserDto user);
	
	UserDto getUser(String email);
	
	UserDto getUserByUserId(String userId);
	
	List<UserDto> getUsers(int page, int limit);
	
	void deleteUser(String userId);
	
	UserVaultDto createVault(String userId, UserVaultDto vault);
	
	List<UserVaultDto> getVaults(String userId);
	
	UserVaultDto getVaultByVaultId(String userId, String vaultId);
	
	UserVaultDto updateVault(String userId, String vaultId, UserVaultDto vaultDetails);
	
	void deleteVault(String userId, String vaultId);
}
