package com.tomaszwasilonek.vaults.ws.service;

import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;

public interface UserService {
	
	UserDto createUser(UserDto user);
}
