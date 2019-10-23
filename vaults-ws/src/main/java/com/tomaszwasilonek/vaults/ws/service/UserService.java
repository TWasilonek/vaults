package com.tomaszwasilonek.vaults.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	UserDto createUser(UserDto user);
	
	UserDto updateUser(String userId, UserDto user);
	
	UserDto getUser(String email);
	
	UserDto getUserByUserId(String userId);
	
	List<UserDto> getUsers(int page, int limit);
	
	void deleteUser(String userId);
	
}
