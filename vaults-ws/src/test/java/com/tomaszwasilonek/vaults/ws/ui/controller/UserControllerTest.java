package com.tomaszwasilonek.vaults.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.TestPropertySource;

import com.tomaszwasilonek.vaults.ws.service.impl.UserServiceImpl;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;
import com.tomaszwasilonek.vaults.ws.ui.model.request.SignUpRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.UserDetailsRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.UserVaultDetailsRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;
import com.tomaszwasilonek.vaults.ws.ui.model.response.UserRest;
import com.tomaszwasilonek.vaults.ws.ui.model.response.UserVaultRest;

@TestPropertySource("/application-dev.properties")
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
		verify(userService, times(1)).getUsers(anyInt(), anyInt());
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
		verify(userService, times(1)).getUserByUserId(anyString());
	}

	@Test
	void testCreateUser() {
		when(userService.createUser(any(UserDto.class))).thenReturn(userData);

		assertDoesNotThrow(() -> {
			UserRest userRest = userController
					.createUser(new SignUpRequestModel(FIRST_NAME, LAST_NAME, EMAIL, RAW_PASSWORD));

			assertNotNull(userRest);
			assertEquals(USER_ID, userRest.getUserId());
			assertEquals(userData.getEmail(), userRest.getEmail());
			assertEquals(userData.getFirstName(), userRest.getFirstName());
			assertEquals(userData.getLastName(), userRest.getLastName());
			verify(userService, times(1)).createUser(any(UserDto.class));
		});
	}

	@Test
	void testCreateUser_missingRequiredFields() {
		assertThrows(IllegalArgumentException.class, () -> {
			userController.createUser(new SignUpRequestModel("", LAST_NAME, EMAIL, RAW_PASSWORD));
		});
		assertThrows(IllegalArgumentException.class, () -> {
			userController.createUser(new SignUpRequestModel(FIRST_NAME, "", EMAIL, RAW_PASSWORD));
		});
		assertThrows(IllegalArgumentException.class, () -> {
			userController.createUser(new SignUpRequestModel(FIRST_NAME, LAST_NAME, "", RAW_PASSWORD));
		});
		assertThrows(IllegalArgumentException.class, () -> {
			userController.createUser(new SignUpRequestModel(FIRST_NAME, LAST_NAME, EMAIL, ""));
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
			verify(userService, times(1)).updateUser(anyString(), any(UserDto.class));
		});
	}
	
	@Test
	void testUpdateUser_missingRequiredFields() {
		assertThrows(IllegalArgumentException.class, () -> {
			UserDetailsRequestModel userDetails = new UserDetailsRequestModel();
			userDetails.setFirstName("");
			userDetails.setLastName("ChangedLastName");
			userDetails.setEmail(EMAIL);
			
			userController.updateUser(USER_ID, userDetails);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			UserDetailsRequestModel userDetails = new UserDetailsRequestModel();
			userDetails.setFirstName("ChangedFirstName");
			userDetails.setLastName("");
			userDetails.setEmail(EMAIL);
			
			userController.updateUser(USER_ID, userDetails);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
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
		verify(userService, times(1)).deleteUser(anyString());
	}
	
	
	
	@Nested
	class UserVaultsTest {
		
		UserVaultDto vaultDetails;
		
		final String VAULT_NAME = "VaultName";
		final String VAULT_ID = "2";
		
		@BeforeEach
		void setUp() {
			vaultDetails = new UserVaultDto();
			vaultDetails.setName(VAULT_NAME);
			vaultDetails.setVaultId(VAULT_ID);
		}
		
		@Test
		void testCreateVault() {		
			when(userService.createVault(anyString(), any(UserVaultDto.class))).thenReturn(vaultDetails);
			
			UserVaultDetailsRequestModel vaultDetailsRequestModel = new UserVaultDetailsRequestModel();
			vaultDetailsRequestModel.setName(VAULT_NAME);
			
			UserVaultRest userVaultsRest = userController.createVault(USER_ID, vaultDetailsRequestModel);
			
			assertNotNull(userVaultsRest);
			assertEquals(vaultDetails.getName(), userVaultsRest.getName());
			assertEquals(vaultDetails.getBalance(), userVaultsRest.getBalance());
			verify(userService, times(1)).createVault(anyString(), any(UserVaultDto.class));
		}
		
		@Test
		void testCreateUserVault_missingRequiredFields() {
			assertThrows(IllegalArgumentException.class, () -> {
				UserVaultDetailsRequestModel vaultDetails = new UserVaultDetailsRequestModel();
				vaultDetails.setName("");
				
				userController.createVault(USER_ID, vaultDetails);
			});
		}
		
		@Test
		void testGetVaults() {
			List<UserVaultDto> mockVaults = new ArrayList<>();
			mockVaults.add(new UserVaultDto());
			mockVaults.add(new UserVaultDto());
			
			when(userService.getVaults(anyString())).thenReturn(mockVaults);
			
			List<UserVaultRest> userVaults = userController.getVaults(USER_ID);
			
			assertEquals(2, userVaults.size());
			verify(userService, times(1)).getVaults(anyString());
		}
		
		@Test
		void testGetVault() {
			when(userService.getVaultByVaultId(anyString(), anyString())).thenReturn(vaultDetails);
			
			UserVaultRest userVaultRest = userController.getVault(USER_ID, VAULT_ID);
			
			assertNotNull(userVaultRest);
			assertEquals(vaultDetails.getName(), userVaultRest.getName());
			verify(userService, times(1)).getVaultByVaultId(anyString(), anyString());
		}
		
		@Test
		void testUpdateVault() {
			UserVaultDto udpdatedVault = new UserVaultDto();
			BeanUtils.copyProperties(vaultDetails, udpdatedVault);
			udpdatedVault.setName("Changed Name");
			
			when(userService.updateVault(anyString(), anyString(), any(UserVaultDto.class))).thenReturn(udpdatedVault);
			
			UserVaultDetailsRequestModel vaultDetailsRequestModel = new UserVaultDetailsRequestModel();
			vaultDetailsRequestModel.setName("Changed Name");
			
			assertDoesNotThrow(() -> {
				UserVaultRest userVaultRest = userController.updateVault(USER_ID, VAULT_ID, vaultDetailsRequestModel);
				assertNotNull(userVaultRest);
				assertEquals("Changed Name", userVaultRest.getName());
				verify(userService, times(1)).updateVault(anyString(), anyString(), any(UserVaultDto.class));
			});		
		}
		
		@Test
		void testUpdateUserVault_missingRequiredFields() {
			assertThrows(IllegalArgumentException.class, () -> {
				UserVaultDetailsRequestModel vaultDetails = new UserVaultDetailsRequestModel();
				vaultDetails.setName("");
				
				userController.updateVault(USER_ID, VAULT_ID, vaultDetails);
			});
		}
		
		@Test
		void testDeleteVault() {
			OperationStatusModel response = userController.deleteVault(USER_ID, VAULT_ID);
			assertNotNull(response);
			assertEquals(RequestOperationName.DELETE.name(), response.getOperationName());
			assertEquals(RequestOperationStatus.SUCCESS.name(), response.getOperationResult());
			verify(userService, times(1)).deleteVault(anyString(), anyString());
		}
	}
}
