package com.tomaszwasilonek.vaults.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import com.tomaszwasilonek.vaults.ws.exceptions.UserServiceException;
import com.tomaszwasilonek.vaults.ws.service.impl.UserServiceImpl;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.ui.model.request.UserDetailsRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;
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
	void testGetUsers() {
		List<UserDto> foundUsers = new ArrayList<>();
		foundUsers.add(new UserDto());
		foundUsers.add(new UserDto());
		foundUsers.add(new UserDto());
		foundUsers.add(new UserDto());

		when(userService.getUsers(anyInt(), anyInt())).thenReturn(foundUsers.subList(0, 2));

		List<UserRest> users = userController.getUsers(1, 2);
		assertNotNull(users);
		assertEquals(2, users.size());
	}

	@Test
	void testGetUser() {
		when(userService.getUserByUserId(anyString())).thenReturn(userData);

		UserRest userRest = userController.getUser(USER_ID);

		assertNotNull(userRest);
		assertEquals(USER_ID, userRest.getUserId());
		assertEquals(userData.getEmail(), userRest.getEmail());
		assertEquals(userData.getFirstName(), userRest.getFirstName());
		assertEquals(userData.getLastName(), userRest.getLastName());
	}

	@Test
	void testCreateUser() {
		when(userService.createUser(any(UserDto.class))).thenReturn(userData);

		assertDoesNotThrow(() -> {
			UserRest userRest = userController
					.createUser(new UserDetailsRequestModel(FIRST_NAME, LAST_NAME, EMAIL, RAW_PASSWORD));

			assertNotNull(userRest);
			assertEquals(USER_ID, userRest.getUserId());
			assertEquals(userData.getEmail(), userRest.getEmail());
			assertEquals(userData.getFirstName(), userRest.getFirstName());
			assertEquals(userData.getLastName(), userRest.getLastName());
		});
	}

	@Test
	void testCreateUser_missingRequiredFields() {
		assertThrows(UserServiceException.class, () -> {
			userController.createUser(new UserDetailsRequestModel("", LAST_NAME, EMAIL, RAW_PASSWORD));
		});
		assertThrows(UserServiceException.class, () -> {
			userController.createUser(new UserDetailsRequestModel(FIRST_NAME, "", EMAIL, RAW_PASSWORD));
		});
		assertThrows(UserServiceException.class, () -> {
			userController.createUser(new UserDetailsRequestModel(FIRST_NAME, LAST_NAME, "", RAW_PASSWORD));
		});
		assertThrows(UserServiceException.class, () -> {
			userController.createUser(new UserDetailsRequestModel(FIRST_NAME, LAST_NAME, EMAIL, ""));
		});
	}

	@Test
	void testUpdateUser() {
		UserDto updatedUserData = new UserDto();
		BeanUtils.copyProperties(userData, updatedUserData);
		updatedUserData.setFirstName("ChangedFirstName");
		updatedUserData.setLastName("ChangedLastName");
		
		when(userService.updateUser(anyString(), any(UserDto.class))).thenReturn(updatedUserData);
		
		UserDetailsRequestModel userDetails = new UserDetailsRequestModel();
		userDetails.setFirstName("ChangedFirstName");
		userDetails.setLastName("ChangedLastName");
		userDetails.setEmail(EMAIL);
		
		assertDoesNotThrow(() -> {
			UserRest userRest = userController.updateUser(USER_ID, userDetails);
			assertNotNull(userRest);
			assertEquals(USER_ID, userRest.getUserId());
			assertEquals(userData.getEmail(), userRest.getEmail());
			assertEquals("ChangedFirstName", userRest.getFirstName());
			assertEquals("ChangedLastName", userRest.getLastName());
		});
	}
	
	@Test
	void testUpdateUser_missingRequiredFields() {
		assertThrows(UserServiceException.class, () -> {
			UserDetailsRequestModel userDetails = new UserDetailsRequestModel();
			userDetails.setFirstName("");
			userDetails.setLastName("ChangedLastName");
			userDetails.setEmail(EMAIL);
			
			userController.updateUser(USER_ID, userDetails);
		});
		
		assertThrows(UserServiceException.class, () -> {
			UserDetailsRequestModel userDetails = new UserDetailsRequestModel();
			userDetails.setFirstName("ChangedFirstName");
			userDetails.setLastName("");
			userDetails.setEmail(EMAIL);
			
			userController.updateUser(USER_ID, userDetails);
		});
		
		assertThrows(UserServiceException.class, () -> {
			UserDetailsRequestModel userDetails = new UserDetailsRequestModel();
			userDetails.setFirstName("ChangedFirstName");
			userDetails.setLastName("ChangedLastName");
			userDetails.setEmail("");
			
			userController.updateUser(USER_ID, userDetails);
		});
	}
	
	@Test
	void testDeleteUser() {
		OperationStatusModel response = userController.deleteUser(USER_ID);
		assertNotNull(response);
		assertEquals(RequestOperationName.DELETE.name(), response.getOperationName());
		assertEquals(RequestOperationStatus.SUCCESS.name(), response.getOperationResult());
	}
}
