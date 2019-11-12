package com.tomaszwasilonek.vaults.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tomaszwasilonek.vaults.ws.exceptions.UserServiceException;
import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.io.repositories.UserRepository;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;

class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	final void testGetUser() {
		// mock user entity
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Tom");
		userEntity.setUserId("jhdsajdhsaf3141");
		userEntity.setEncryptedPassword("jhdsa6hbsa6boi838");
		
		// mock userRepository methods used in getUser()
		when(userRepository.findByEmail(anyString()))
			.thenReturn(userEntity);
		
		
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals("Tom", userDto.getFirstName());
	}
	
	@Test
	final void testGetUser_UserServiceException() {
		when(userRepository.findByEmail(anyString()))
			.thenReturn(null);
		
		assertThrows(UserServiceException.class, () -> {
			userService.getUser("test@test.pl");
		});
	}

}
