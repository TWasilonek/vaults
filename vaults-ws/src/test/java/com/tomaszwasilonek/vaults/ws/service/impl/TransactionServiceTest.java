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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tomaszwasilonek.vaults.ws.entity.Transaction;
import com.tomaszwasilonek.vaults.ws.entity.UserVault;
import com.tomaszwasilonek.vaults.ws.repositories.TransactionRepository;
import com.tomaszwasilonek.vaults.ws.shared.dto.InternalTransactionDTO;

public class TransactionServiceTest {
	
	@InjectMocks
	TransactionServiceImpl transactionService;
	
	@Mock
	TransactionRepository transactionRepository;
	
	@Mock
	UserVaultServiceImpl userVaultService;
	
	final String SOURCE_VAULT_ID = "sourceVaultId";
	final String TARGET_VAULT_ID = "destinationVaultId";
	
	Transaction transaction;
	InternalTransactionDTO internalTransactionDTO;
	
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		transaction = new Transaction();
		transaction.setAmount(10.00);
		transaction.setSourceVaultId(SOURCE_VAULT_ID);
		transaction.setTargetVaultId(TARGET_VAULT_ID);
	
		internalTransactionDTO = new InternalTransactionDTO();
		internalTransactionDTO.setAmount(10.00);
		internalTransactionDTO.setSourceVaultId(SOURCE_VAULT_ID);
		internalTransactionDTO.setTargetVaultId(TARGET_VAULT_ID);
	}
	
	@Test
	void testMakeInternalTransaction() {
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
		
		InternalTransactionDTO storedTransaction = transactionService.makeInternalTransaction(internalTransactionDTO);
		
		assertEquals(10.00, storedTransaction.getAmount());
		assertEquals(SOURCE_VAULT_ID, storedTransaction.getSourceVaultId());
		assertEquals(TARGET_VAULT_ID, storedTransaction.getTargetVaultId());
		
		verify(transactionRepository, times(1)).save(any(Transaction.class));
		verify(userVaultService, times(1)).applyInternalTransaction(any(InternalTransactionDTO.class));
	}

}
