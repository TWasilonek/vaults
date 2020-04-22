package com.tomaszwasilonek.vaults.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.tomaszwasilonek.vaults.ws.service.impl.TransactionServiceImpl;
import com.tomaszwasilonek.vaults.ws.shared.dto.InternalTransactionDTO;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;

public class TransactionControllerTest {
	
	@InjectMocks
	TransactionController transactionController;
	
	@Mock
	TransactionServiceImpl transactionService;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testPostInternalTransaction() {
		InternalTransactionDTO transaction = new InternalTransactionDTO();
		transaction.setAmount(200);
		transaction.setSourceVaultId("sourceVaultId");
		transaction.setTargetVaultId("targetVaultId");
		
		when(transactionService.makeInternalTransaction(any(InternalTransactionDTO.class))).thenReturn(transaction);
		
		OperationStatusModel response = transactionController.postInternalTransaction(transaction);
		assertNotNull(response);
		assertEquals(RequestOperationName.INTERNAL_TRANSACTION.name(), response.getOperationName());
		assertEquals(RequestOperationStatus.SUCCESS.name(), response.getOperationResult());
		verify(transactionService, times(1)).makeInternalTransaction(any(InternalTransactionDTO.class));
	}
}
