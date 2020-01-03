package com.tomaszwasilonek.vaults.ws.service.impl;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
		// mock userRepository methods used in getUser()
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals(userEntity.getFirstName(), userDto.getFirstName());
		assertEquals(userEntity.getLastName(), userDto.getLastName());
		assertEquals(userEntity.getEmail(), userDto.getEmail());
	}
	
	@Test
	final void testGetUser_UserNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(UserServiceException.class, () -> {
			userService.getUser(EMAIL);
		});
	}
	
	@Test
	final void testCreateUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateUserId(anyInt())).thenReturn(USER_ID);
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
		verify(utils, times(1)).generateUserId(anyInt());
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}
	
	@Test
	final void testCreateUser_UserAlreadyExistsException() {
		when(userRepository.findByEmail(anyString()))
			.thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setEmail(EMAIL);
		
		assertThrows(UserServiceException.class, () -> {
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
	final void testUpdateUser_UserNotFoundException() {
		when(userRepository.findByUserId(anyString())).thenReturn(null);
		
		assertThrows(UserServiceException.class, () -> {
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
	final void testDeleteUser_UserNotFoundException() {
		when(userRepository.findByUserId(anyString())).thenReturn(null);
		
		assertThrows(UserServiceException.class, () -> {
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
}
