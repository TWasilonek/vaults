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

import com.tomaszwasilonek.vaults.ws.service.impl.PaymentServiceImpl;
import com.tomaszwasilonek.vaults.ws.shared.dto.MoneyTransferDTO;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;

public class PaymentControllerTest {
	
	@InjectMocks
	PaymentController paymentController;
	
	@Mock
	PaymentServiceImpl paymentService;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testMakeMoneyTransfer() {
		MoneyTransferDTO moneyTransfer = new MoneyTransferDTO();
		moneyTransfer.setAmount(200);
		moneyTransfer.setSourceAccount("sourceVaultId");
		moneyTransfer.setDestinationAccount("targetVaultId");
		
		when(paymentService.makeMoneyTransfer(any(MoneyTransferDTO.class))).thenReturn(moneyTransfer);
		
		OperationStatusModel response = paymentController.makeMoneyTransfer(moneyTransfer);
		assertNotNull(response);
		assertEquals(RequestOperationName.MONEY_TRANSFER.name(), response.getOperationName());
		assertEquals(RequestOperationStatus.SUCCESS.name(), response.getOperationResult());
		verify(paymentService, times(1)).makeMoneyTransfer(any(MoneyTransferDTO.class));
	}
}
