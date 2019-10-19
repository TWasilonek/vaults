package com.tomaszwasilonek.vaults.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	UserDto createUser(UserDto user);
}
