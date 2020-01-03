package com.tomaszwasilonek.vaults.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tomaszwasilonek.vaults.ws.service.impl.UserServiceImpl;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.ui.model.response.UserRest;

class UserControllerTest {
	
	@InjectMocks
	UserController userController;
	
	@Mock
	UserServiceImpl userService;
	
	final String USER_ID = "abcde1234";
	final String RAW_PASSWORD = "jhdsa6hbsa6boi838";
	final String EMAIL = "test@test.com";
	final String FIRST_NAME = "Tom";
	final String LAST_NAME = "Tester";
	
	UserDto userData;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userData = new UserDto();
		userData.setId(1L);
		userData.setFirstName(FIRST_NAME);
		userData.setLastName(LAST_NAME);
		userData.setUserId(USER_ID);
		userData.setEncryptedPassword(RAW_PASSWORD);
		userData.setEmail(EMAIL);
	}

	@Test
	void testGetUser() {
		when(userService.getUserByUserId(anyString())).thenReturn(userData);
		
		UserRest userRest =  userController.getUser(USER_ID);
		
		assertNotNull(userRest);
		assertEquals(USER_ID, userRest.getUserId());
		assertEquals(userData.getEmail(), userRest.getEmail());
		assertEquals(userData.getFirstName(), userRest.getFirstName());
		assertEquals(userData.getLastName(), userRest.getLastName());
	}
	
	// TODO: add all UserController tests

}
