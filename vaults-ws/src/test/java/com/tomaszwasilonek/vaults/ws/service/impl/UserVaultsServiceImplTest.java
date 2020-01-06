package com.tomaszwasilonek.vaults.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import com.tomaszwasilonek.vaults.ws.exceptions.VaultsServiceException;
import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.io.entity.UserVaultsEntity;
import com.tomaszwasilonek.vaults.ws.io.repositories.UserVaultsRepository;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultsDto;

class UserVaultsServiceImplTest {
	
	@InjectMocks
	UserVaultsServiceImpl userVaultsService;
	
	@Mock
	UserVaultsRepository userVaultsRepository;
	
	@Mock
	Utils utils;
	
	final String VAULT_ID = "123";
	final String NAME = "VaultName";
	final double BALANCE = 0.00;
	
	UserVaultsEntity userVaultsEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userVaultsEntity = new UserVaultsEntity();
		userVaultsEntity.setBalance(BALANCE);
		userVaultsEntity.setName(NAME);
		userVaultsEntity.setVaultId(VAULT_ID);
	}

	@Test
	void testCreateVault() {
		when(utils.generateVaultId(anyInt())).thenReturn(VAULT_ID);
		when(userVaultsRepository.save(any(UserVaultsEntity.class))).thenReturn(userVaultsEntity);
		
		UserVaultsDto storedVault = userVaultsService.createVault(new UserEntity(), new UserVaultsDto());
	
		assertNotNull(storedVault);
		assertEquals(userVaultsEntity.getName(), storedVault.getName());
		assertEquals(userVaultsEntity.getBalance(), storedVault.getBalance());
		assertEquals(userVaultsEntity.getVaultId(), storedVault.getVaultId());
		verify(utils, times(1)).generateVaultId(anyInt());
		verify(userVaultsRepository, times(1)).save(any(UserVaultsEntity.class));
	}

	@Test
	void testCreateVault_duplicateName() {
		UserVaultsDto userVaultsDto = new UserVaultsDto();
		userVaultsDto.setName("Duplicated name");
		
		when(userVaultsRepository.findByName(anyString())).thenReturn(new UserVaultsEntity());
		
		assertThrows(VaultsServiceException.class, () -> {
			userVaultsService.createVault(new UserEntity(), userVaultsDto);
		});
	}
	
	@Test
	void testGetVaults() {
		List<UserVaultsEntity> vaults = new ArrayList<>();
		vaults.add(new UserVaultsEntity());
		vaults.add(new UserVaultsEntity());
		
		when(userVaultsRepository.findAllByUserDetails(any(UserEntity.class))).thenReturn(vaults);
		
		List<UserVaultsDto> storedVaults = userVaultsService.getVaults(new UserEntity());
		
		assertNotNull(storedVaults);
		assertEquals(2, storedVaults.size());
		verify(userVaultsRepository, times(1)).findAllByUserDetails(any(UserEntity.class));
	}

	@Test
	void testGetVault() {
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(userVaultsEntity);
		
		UserVaultsDto storedVault = userVaultsService.getVault(VAULT_ID);
		
		assertNotNull(storedVault);
		assertEquals(userVaultsEntity.getName(), storedVault.getName());
		assertEquals(userVaultsEntity.getBalance(), storedVault.getBalance());
		assertEquals(userVaultsEntity.getVaultId(), storedVault.getVaultId());
		verify(userVaultsRepository, times(1)).findByVaultId(anyString());
	}
	
	@Test
	void testGetVault_vaultNotFound() {
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(null);
		
		assertThrows(VaultsServiceException.class, () -> {
			userVaultsService.getVault(VAULT_ID);
		});
	}

	@Test
	void testUpdateVault() {
		UserVaultsEntity newUserVaultsEntity = new UserVaultsEntity();
		BeanUtils.copyProperties(userVaultsEntity, newUserVaultsEntity);
		newUserVaultsEntity.setName("Test");
		newUserVaultsEntity.setBalance(10);
		
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(userVaultsEntity);
		when(userVaultsRepository.save(any(UserVaultsEntity.class))).thenReturn(newUserVaultsEntity);
		
		UserVaultsDto updatedVault = userVaultsService.updateVault(VAULT_ID, new UserVaultsDto());
		
		assertNotNull(updatedVault);
		assertEquals("Test", updatedVault.getName());
		assertEquals(10, updatedVault.getBalance());
		verify(userVaultsRepository, times(1)).findByVaultId(anyString());
		verify(userVaultsRepository, times(1)).save(any(UserVaultsEntity.class));
	}
	
	@Test
	void testUpdateVault_vaultNotFound() {
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(null);
		
		assertThrows(VaultsServiceException.class, () -> {
			userVaultsService.updateVault(VAULT_ID, new UserVaultsDto());
		});
	}

	@Test
	void testDeleteVault() {
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(userVaultsEntity);
		
		assertDoesNotThrow(() -> {
			userVaultsService.deleteVault(VAULT_ID);
			verify(userVaultsRepository, times(1)).delete(userVaultsEntity);
		});
	}
	
	@Test
	void testDeleteVault_vaultNotFound() {
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(null);
		
		assertThrows(VaultsServiceException.class, () -> {
			userVaultsService.deleteVault(VAULT_ID);
		});
	}

}
