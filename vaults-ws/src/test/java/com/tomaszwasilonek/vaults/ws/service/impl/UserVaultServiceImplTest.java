package com.tomaszwasilonek.vaults.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
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

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.entity.UserVault;
import com.tomaszwasilonek.vaults.ws.exceptions.BalanceTooLowException;
import com.tomaszwasilonek.vaults.ws.exceptions.EntityNotFoundException;
import com.tomaszwasilonek.vaults.ws.exceptions.RecordAlreadyExistsException;
import com.tomaszwasilonek.vaults.ws.repositories.UserVaultRepository;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;

@TestPropertySource("/application-dev.properties")
class UserVaultServiceImplTest {
	
	@InjectMocks
	UserVaultServiceImpl userVaultsService;
	
	@Mock
	UserVaultRepository userVaultsRepository;
	
	@Mock
	Utils utils;
	
	final String VAULT_ID = "123";
	final String NAME = "VaultName";
	final double BALANCE = 0.00;
	
	UserVault userVaultsEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userVaultsEntity = new UserVault();
		userVaultsEntity.setBalance(BALANCE);
		userVaultsEntity.setName(NAME);
		userVaultsEntity.setVaultId(VAULT_ID);
	}

	@Test
	void testCreateVault() {
		when(userVaultsRepository.save(any(UserVault.class))).thenReturn(userVaultsEntity);
		
		UserVaultDto storedVault = userVaultsService.createVault(new UserEntity(), new UserVaultDto());
	
		assertNotNull(storedVault);
		assertEquals(userVaultsEntity.getName(), storedVault.getName());
		assertEquals(userVaultsEntity.getBalance(), storedVault.getBalance());
		assertEquals(userVaultsEntity.getVaultId(), storedVault.getVaultId());
		verify(userVaultsRepository, times(1)).save(any(UserVault.class));
	}

	@Test
	void testCreateVault_duplicateName() {
		UserVaultDto userVaultsDto = new UserVaultDto();
		userVaultsDto.setName("Duplicated name");
		
		when(userVaultsRepository.findByName(anyString())).thenReturn(new UserVault());
		
		assertThrows(RecordAlreadyExistsException.class, () -> {
			userVaultsService.createVault(new UserEntity(), userVaultsDto);
		});
	}
	
	@Test
	void testGetVaults() {
		List<UserVault> vaults = new ArrayList<>();
		vaults.add(new UserVault());
		vaults.add(new UserVault());
		
		when(userVaultsRepository.findAllByUserDetails(any(UserEntity.class))).thenReturn(vaults);
		
		List<UserVaultDto> storedVaults = userVaultsService.getVaults(new UserEntity());
		
		assertNotNull(storedVaults);
		assertEquals(2, storedVaults.size());
		verify(userVaultsRepository, times(1)).findAllByUserDetails(any(UserEntity.class));
	}

	@Test
	void testGetVault() {
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(userVaultsEntity);
		
		UserVaultDto storedVault = userVaultsService.getVault(VAULT_ID);
		
		assertNotNull(storedVault);
		assertEquals(userVaultsEntity.getName(), storedVault.getName());
		assertEquals(userVaultsEntity.getBalance(), storedVault.getBalance());
		assertEquals(userVaultsEntity.getVaultId(), storedVault.getVaultId());
		verify(userVaultsRepository, times(1)).findByVaultId(anyString());
	}
	
	@Test
	void testGetVault_vaultNotFound() {
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(null);
		
		assertThrows(EntityNotFoundException.class, () -> {
			userVaultsService.getVault(VAULT_ID);
		});
	}

	@Test
	void testUpdateVault() {
		UserVault newUserVaultsEntity = new UserVault();
		BeanUtils.copyProperties(userVaultsEntity, newUserVaultsEntity);
		newUserVaultsEntity.setName("Test");
		newUserVaultsEntity.setBalance(10);
		
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(userVaultsEntity);
		when(userVaultsRepository.save(any(UserVault.class))).thenReturn(newUserVaultsEntity);
		
		UserVaultDto updatedVault = userVaultsService.updateVault(VAULT_ID, new UserVaultDto());
		
		assertNotNull(updatedVault);
		assertEquals("Test", updatedVault.getName());
		assertEquals(10, updatedVault.getBalance());
		verify(userVaultsRepository, times(1)).findByVaultId(anyString());
		verify(userVaultsRepository, times(1)).save(any(UserVault.class));
	}
	
	@Test
	void testUpdateVault_vaultNotFound() {
		when(userVaultsRepository.findByVaultId(anyString())).thenReturn(null);
		
		assertThrows(EntityNotFoundException.class, () -> {
			userVaultsService.updateVault(VAULT_ID, new UserVaultDto());
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
		
		assertThrows(EntityNotFoundException.class, () -> {
			userVaultsService.deleteVault(VAULT_ID);
		});
	}
	
	@Nested
	class TestMoneyTransfer {
		UserVault sourceVault;
		UserVault targetVault;
		PaymentDTO payment;
		
		@BeforeEach
		void setUp() throws Exception {
			sourceVault = new UserVault();
			BeanUtils.copyProperties(userVaultsEntity, sourceVault);
			sourceVault.setVaultId("source");
			sourceVault.setBalance(10.00);
			
			targetVault = new UserVault();
			BeanUtils.copyProperties(userVaultsEntity, targetVault);
			targetVault.setVaultId("target");
			targetVault.setBalance(20.00);
			
			payment = new PaymentDTO();
			payment.setAmount(10.00);
			payment.setSourceAccount("source");
			payment.setDestinationAccount("target");
		}
		
		@Test
		void testMoneyTransfer_HappyPath() {
			when(userVaultsRepository.findByVaultId("source")).thenReturn(sourceVault);
			when(userVaultsRepository.findByVaultId("target")).thenReturn(targetVault);
	 		
			userVaultsService.moneyTransfer(payment);
			
			verify(userVaultsRepository, times(1)).findByVaultId("source");
			verify(userVaultsRepository, times(1)).findByVaultId("target");
			verify(userVaultsRepository, times(2)).save(any(UserVault.class));
			verify(userVaultsRepository).save(argThat(
					(UserVault aVault) -> aVault.getVaultId() == "source" && aVault.getBalance() == 0));
			verify(userVaultsRepository).save(argThat(
					(UserVault aVault) -> aVault.getVaultId() == "target" && aVault.getBalance() == 30.00));
		}
		
		@Test
		void testMoneyTransfer_sourceVaultNotFound() {	
			when(userVaultsRepository.findByVaultId("source")).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userVaultsService.moneyTransfer(payment);
			});
		}
		
		@Test
		void testMoneyTransfer_targetVaultNotFound() {			
			when(userVaultsRepository.findByVaultId("target")).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userVaultsService.moneyTransfer(payment);
			});
		}
		
		@Test
		void testMoneyTransfer_sourceVaultBalanceTooLow() {
			sourceVault.setBalance(0);
			
			when(userVaultsRepository.findByVaultId("source")).thenReturn(sourceVault);
			when(userVaultsRepository.findByVaultId("target")).thenReturn(targetVault);
			
			assertThrows(BalanceTooLowException.class, () -> {
				userVaultsService.moneyTransfer(payment);
			});
		}
	}
	
	@Nested
	class testDeposit {
		UserVault targetVault;
		PaymentDTO payment;
		
		@BeforeEach
		void setUp() throws Exception {			
			targetVault = new UserVault();
			BeanUtils.copyProperties(userVaultsEntity, targetVault);
			targetVault.setVaultId("target");
			targetVault.setBalance(20.00);
			
			payment = new PaymentDTO();
			payment.setAmount(10.00);
			payment.setSourceAccount("source");
			payment.setDestinationAccount("target");
		}
		
		@Test
		void testDeposit_HappyPath() {
			when(userVaultsRepository.findByVaultId("target")).thenReturn(targetVault);
	 		
			userVaultsService.deposit(payment);
			
			verify(userVaultsRepository, times(1)).findByVaultId("target");
			verify(userVaultsRepository, times(1)).save(any(UserVault.class));
			verify(userVaultsRepository).save(argThat(
					(UserVault aVault) -> aVault.getVaultId() == "target" && aVault.getBalance() == 30.00));
		}
		
		@Test
		void testDeposit_targetVaultNotFound() {			
			when(userVaultsRepository.findByVaultId("target")).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userVaultsService.deposit(payment);
			});
		}
	}
	
	@Nested
	class testWithdrawal {
		UserVault sourceVault;
		PaymentDTO payment;
		
		@BeforeEach
		void setUp() throws Exception {			
			sourceVault = new UserVault();
			BeanUtils.copyProperties(userVaultsEntity, sourceVault);
			sourceVault.setVaultId("source");
			sourceVault.setBalance(20.00);
			
			payment = new PaymentDTO();
			payment.setAmount(10.00);
			payment.setSourceAccount("source");
			payment.setDestinationAccount("target");
		}
		
		@Test
		void testWithdrawal_HappyPath() {
			when(userVaultsRepository.findByVaultId("source")).thenReturn(sourceVault);
	 		
			userVaultsService.withdraw(payment);
			
			verify(userVaultsRepository, times(1)).findByVaultId("source");
			verify(userVaultsRepository, times(1)).save(any(UserVault.class));
			verify(userVaultsRepository).save(argThat(
					(UserVault aVault) -> aVault.getVaultId() == "source" && aVault.getBalance() == 10.00));
		}
		
		@Test
		void testWithdrawal_sourceVaultNotFound() {			
			when(userVaultsRepository.findByVaultId("source")).thenReturn(null);
			
			assertThrows(EntityNotFoundException.class, () -> {
				userVaultsService.withdraw(payment);
			});
		}
	}
}
