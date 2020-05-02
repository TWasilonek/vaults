package com.tomaszwasilonek.vaults.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.exceptions.EntityNotFoundException;
import com.tomaszwasilonek.vaults.ws.exceptions.RecordAlreadyExistsException;
import com.tomaszwasilonek.vaults.ws.repositories.UserRepository;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;

@TestPropertySource("/application-dev.properties")
class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	UserVaultService userVaultService;
	
	final String USER_ID = "abcde1234";
	final String RAW_PASSWORD = "jhdsa6hbsa6boi838";
	final String EMAIL = "test@test.com";
	final String FIRST_NAME = "Tom";
	final String LAST_NAME = "Tester";
	
	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		// mock user entity
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName(FIRST_NAME);
		userEntity.setLastName(LAST_NAME);
		userEntity.setUserId(USER_ID);
		userEntity.setEncryptedPassword(RAW_PASSWORD);
		userEntity.setEmail(EMAIL);
	}

	@Test
	final void testGetUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals(userEntity.getFirstName(), userDto.getFirstName());
		assertEquals(userEntity.getLastName(), userDto.getLastName());
		assertEquals(userEntity.getEmail(), userDto.getEmail());
	}
	
	@Test
	final void testGetUser_EntityNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(EntityNotFoundException.class, () -> {
			userService.getUser("test@test.com");
		});
	}
	
	@Test
	final void testGetUserByUserId() {
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUserByUserId("testId");
		
		assertNotNull(userDto);
		assertEquals(userEntity.getFirstName(), userDto.getFirstName());
		assertEquals(userEntity.getLastName(), userDto.getLastName());
		assertEquals(userEntity.getEmail(), userDto.getEmail());
	}
	
	@Test
	final void testGetUserByUserId_EntityNotFoundException() {
		when(userRepository.findByUserId(anyString())).thenReturn(null);
		
		assertThrows(EntityNotFoundException.class, () -> {
			userService.getUserByUserId("testId");
		});
	}
	
	@Test
	final void testLoadUserByUsername() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDetails userDetails = userService.loadUserByUsername("test");
		
		assertNotNull(userDetails);
		assertEquals(userEntity.getEmail(), userDetails.getUsername());
		assertEquals(userEntity.getEncryptedPassword(), userDetails.getPassword());
	}
	
	@Test
	final void testLoadUserByUsername_EntityNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(EntityNotFoundException.class, () -> {
			userService.loadUserByUsername("test");
		});
	}
	
	@Test
	final void testCreateUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(RAW_PASSWORD);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setPassword(RAW_PASSWORD);
		UserDto storedUserDetails = userService.createUser(userDto);
	
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertEquals(userEntity.getEmail(), storedUserDetails.getEmail());
		verify(bCryptPasswordEncoder, times(1)).encode(RAW_PASSWORD);
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}
	
	@Test
	final void testCreateUser_UserAlreadyExistsException() {
		when(userRepository.findByEmail(anyString()))
			.thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(EMAIL);
		
		assertThrows(RecordAlreadyExistsException.class, () -> {
			userService.createUser(userDto);
		});
	}
	
	@Test
	final void testUpdateUser() {
		UserEntity newUserEntity = new UserEntity();
		BeanUtils.copyProperties(userEntity, newUserEntity);
		newUserEntity.setFirstName("Test");
		newUserEntity.setLastName("Last");
		
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		when(userRepository.save(any(UserEntity.class))).thenReturn(newUserEntity);
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(newUserEntity, userDto);
		
		UserDto updatedUserDto = userService.updateUser(userEntity.getUserId(), userDto);
		
		assertNotNull(updatedUserDto);
		assertEquals("Test", updatedUserDto.getFirstName());
		assertEquals("Last", updatedUserDto.getLastName());
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}
	
	@Test
	final void testUpdateUser_EntityNotFoundException() {
		when(userRepository.findByUserId(anyString())).thenReturn(null);
		
		assertThrows(EntityNotFoundException.class, () -> {
			userService.updateUser(USER_ID, new UserDto());
		});
	}
	
	@Test
	final void testDeleteUser() {
		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
		
		assertDoesNotThrow(() -> {
			userService.deleteUser(USER_ID);
			verify(userRepository, times(1)).delete(userEntity);
		});
		
	}
	
	@Test
	final void testDeleteUser_EntityNotFoundException() {
		when(userRepository.findByUserId(anyString())).thenReturn(null);
		
		assertThrows(EntityNotFoundException.class, () -> {
			userService.deleteUser(USER_ID);
		});
	}
	
	@Nested
	class TestGetUsers {
		
		List<UserEntity> usersList;
		
		@BeforeEach
		void setUpUsers() {
			usersList = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				UserEntity user = new UserEntity();
				BeanUtils.copyProperties(userEntity, user);
				user.setFirstName("TestUser" + i);
				user.setEmail("TestUser" + i + "@test.com");
				
				usersList.add(user);
			}
		}
		
		@Test
		final void testGetUsers_getJustFirstUser() {
			List<UserEntity> foundUsers = new ArrayList<>();
			foundUsers.add(usersList.get(0));
			
			Page<UserEntity> pagedResponse = new PageImpl<UserEntity>(foundUsers);
			
			when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedResponse);
			
			List<UserDto> foundUsersList = userService.getUsers(1, 1);
			
			assertNotNull(foundUsersList);
			verify(userRepository, times(1)).findAll(any(Pageable.class));
			
			assertEquals(1, foundUsersList.size());
			assertEquals("TestUser0", foundUsersList.get(0).getFirstName());
			assertEquals("TestUser0@test.com", foundUsersList.get(0).getEmail());
		}
		
		@Test
		final void testGetUsers_getFirstPage() {
			List<UserEntity> foundUsers = new ArrayList<>();
			foundUsers.add(usersList.get(0));
			foundUsers.add(usersList.get(1));
			
			Page<UserEntity> pagedResponse = new PageImpl<UserEntity>(foundUsers);
			
			when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedResponse);
			
			List<UserDto> foundUsersList = userService.getUsers(1, 2);
			
			assertEquals(2, foundUsersList.size());
			assertEquals("TestUser0", foundUsersList.get(0).getFirstName());
			assertEquals("TestUser0@test.com", foundUsersList.get(0).getEmail());
			assertEquals("TestUser1", foundUsersList.get(1).getFirstName());
			assertEquals("TestUser1@test.com", foundUsersList.get(1).getEmail());
		}
		
		@Test
		final void testGetUsers_getSecondPage() {
			List<UserEntity> foundUsers = new ArrayList<>();
			foundUsers.add(usersList.get(2));
			foundUsers.add(usersList.get(3));
			
			Page<UserEntity> pagedResponse = new PageImpl<UserEntity>(foundUsers);
			
			when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedResponse);
			
			List<UserDto> foundUsersList = userService.getUsers(2, 2);
			
			assertEquals(2, foundUsersList.size());
			assertEquals("TestUser2", foundUsersList.get(0).getFirstName());
			assertEquals("TestUser2@test.com", foundUsersList.get(0).getEmail());
			assertEquals("TestUser3", foundUsersList.get(1).getFirstName());
			assertEquals("TestUser3@test.com", foundUsersList.get(1).getEmail());
		}
	}
	
	@Nested
	class TestUserVaults {
		
		final String VAULT_ID = "testVaultId";
		final String VAULT_NAME = "vault";
		
		UserVaultDto vaultDto;

		@BeforeEach
		void setUp() throws Exception {			
			// mock vault
			vaultDto = new UserVaultDto();
			vaultDto.setId(1L);
			vaultDto.setVaultId(VAULT_ID);
			vaultDto.setName(VAULT_NAME);
		}
		
		@Test
		final void testCreateVault() {
			when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
			when(userVaultService.createVault(any(UserEntity.class), any(UserVaultDto.class))).thenReturn(vaultDto);
			
			UserVaultDto storedVaultDto = userService.createVault(USER_ID, vaultDto);
		
			assertNotNull(storedVaultDto);
			assertEquals(vaultDto.getName(), storedVaultDto.getName());
			assertEquals(vaultDto.getVaultId(), storedVaultDto.getVaultId());
			assertEquals(0.00, storedVaultDto.getBalance());

			verify(userRepository, times(1)).findByUserId(anyString());
			verify(userVaultService, times(1)).createVault(any(UserEntity.class), any(UserVaultDto.class));
		}
		
		@Test
		final void testCreateVault_EntityNotFoundException() {
			when(userRepository.findByUserId(anyString())).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userService.createVault(USER_ID, vaultDto);
			});
		}
		
		@Test
		final void testUpdateVault() {
			UserVaultDto newVaultDto = new UserVaultDto();
			BeanUtils.copyProperties(vaultDto, newVaultDto);
			newVaultDto.setName("new name");
			
			when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
			when(userVaultService.updateVault(anyString(), any(UserVaultDto.class))).thenReturn(newVaultDto);
			
			UserVaultDto storedVaultDto = userService.updateVault(USER_ID, VAULT_ID, newVaultDto);
		
			assertNotNull(storedVaultDto);
			assertEquals(newVaultDto.getName(), storedVaultDto.getName());
			assertEquals(newVaultDto.getVaultId(), storedVaultDto.getVaultId());
			assertEquals(0.00, storedVaultDto.getBalance());

			verify(userRepository, times(1)).findByUserId(anyString());
			verify(userVaultService, times(1)).updateVault(anyString(), any(UserVaultDto.class));
		}
		
		@Test
		final void testUpdateVault_EntityNotFoundException() {
			when(userRepository.findByUserId(anyString())).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userService.updateVault(USER_ID, "newId", vaultDto);
			});
		}
		
		@Test
		final void testDeleteVault() {			
			when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
			
			userService.deleteVault(USER_ID, VAULT_ID);

			verify(userRepository, times(1)).findByUserId(anyString());
			verify(userVaultService, times(1)).deleteVault(anyString());
		}
		
		@Test
		final void testDeleteVault_EntityNotFoundException() {
			when(userRepository.findByUserId(anyString())).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userService.deleteVault(USER_ID, VAULT_ID);
			});
		}
		
		@Test
		final void testGetVaults() {
			UserVaultDto vaultDto2 = new UserVaultDto();
			vaultDto2.setId(1L);
			vaultDto2.setVaultId("test2");
			vaultDto2.setName("test2");
			
			List<UserVaultDto> vaults = new ArrayList<>();
			vaults.add(vaultDto);
			vaults.add(vaultDto2);
			
			when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
			when(userVaultService.getVaults(any(UserEntity.class))).thenReturn(vaults);
			
			List<UserVaultDto> foundVaults = userService.getVaults(USER_ID);
			
			assertNotNull(foundVaults);
			assertEquals(2, foundVaults.size());
			assertEquals(vaultDto.getName(), foundVaults.get(0).getName());
			assertEquals(vaultDto2.getName(), foundVaults.get(1).getName());

			verify(userRepository, times(1)).findByUserId(anyString());
			verify(userVaultService, times(1)).getVaults(any(UserEntity.class));
		}
		
		
		@Test
		final void testGetVaults_EntityNotFoundException() {
			when(userRepository.findByUserId(anyString())).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userService.getVaults(USER_ID);
			});
		}
		
		@Test
		final void testGetVaultById() {
			when(userRepository.findByUserId(anyString())).thenReturn(userEntity);
			when(userVaultService.getVault(anyString())).thenReturn(vaultDto);
			
			UserVaultDto foundVault = userService.getVaultByVaultId(USER_ID, VAULT_ID);
			
			assertNotNull(foundVault);
			assertEquals(vaultDto.getName(), foundVault.getName());
			assertEquals(vaultDto.getVaultId(), foundVault.getVaultId());
			assertEquals(vaultDto.getBalance(), foundVault.getBalance());

			verify(userRepository, times(1)).findByUserId(anyString());
			verify(userVaultService, times(1)).getVault(anyString());
		}
		
		@Test
		final void testGetVaultById_EntityNotFoundException() {
			when(userRepository.findByUserId(anyString())).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userService.getVaultByVaultId(USER_ID, VAULT_ID);
			});
		}
	}
}
