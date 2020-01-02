package com.tomaszwasilonek.vaults.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tomaszwasilonek.vaults.ws.exceptions.UserServiceException;
import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.io.repositories.UserRepository;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;

class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	String userId = "abcde1234";
	String rawPassword = "jhdsa6hbsa6boi838";
	String email = "test@test.com";
	String name = "Tom";
	String lastName = "Tester";
	
	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		// mock user entity
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName(name);
		userEntity.setLastName(lastName);
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(rawPassword);
		userEntity.setEmail(email);
	}

	@Test
	final void testGetUser() {
		// mock userRepository methods used in getUser()
		when(userRepository.findByEmail(anyString()))
			.thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals(userEntity.getFirstName(), userDto.getFirstName());
		assertEquals(userEntity.getLastName(), userDto.getLastName());
		assertEquals(userEntity.getEmail(), userDto.getEmail());
	}
	
	@Test
	final void testGetUser_UserNotFoundException() {
		when(userRepository.findByEmail(anyString()))
			.thenReturn(null);
		
		assertThrows(UserServiceException.class, () -> {
			userService.getUser(email);
		});
	}
	
	@Test
	final void testCreateUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(rawPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setPassword(rawPassword);
		UserDto storedUserDetails = userService.createUser(userDto);
	
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertEquals(userEntity.getEmail(), storedUserDetails.getEmail());
		verify(bCryptPasswordEncoder, times(1)).encode(rawPassword);
		verify(utils, times(1)).generateUserId(anyInt());
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}
	
	@Test
	final void testCreateUser_RecordAlreadyExistsException() {
		when(userRepository.findByEmail(anyString()))
			.thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(email);
		
		assertThrows(UserServiceException.class, () -> {
			userService.createUser(userDto);
		});
	}
	
	// TODO: USER: Test Delete and Update
	// TODO: VAULTS: Test CRUD

}
