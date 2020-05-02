package com.tomaszwasilonek.vaults.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tomaszwasilonek.vaults.ws.service.impl.PaymentServiceImpl;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;
import com.tomaszwasilonek.vaults.ws.ui.model.request.DepositRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.MoneyTransferRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.WithdrawalRequestModel;
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
		MoneyTransferRequestModel moneyTransferRequest = new MoneyTransferRequestModel();
		moneyTransferRequest.setAmount(200);
		moneyTransferRequest.setSourceAccount("sourceVaultId");
		moneyTransferRequest.setDestinationAccount("targetVaultId");
		
		PaymentDTO moneyTransfer = new PaymentDTO();
		moneyTransfer.setAmount(200);
		moneyTransfer.setSourceAccount("sourceVaultId");
		moneyTransfer.setDestinationAccount("targetVaultId");
		
		when(paymentService.moneyTransfer(any(PaymentDTO.class))).thenReturn(moneyTransfer);
		
		OperationStatusModel response = paymentController.makeMoneyTransfer(moneyTransferRequest);
		
		assertNotNull(response);
		assertEquals(RequestOperationName.MONEY_TRANSFER.name(), response.getOperationName());
		assertEquals(RequestOperationStatus.SUCCESS.name(), response.getOperationResult());
		verify(paymentService, times(1)).moneyTransfer(any(PaymentDTO.class));
	}
	
	@Test
	void testMakeDepoist() {
		DepositRequestModel depositRequest = new DepositRequestModel();
		depositRequest.setAmount(10);
		depositRequest.setDestinationAccount("targetVaultId");
		depositRequest.setSourceSubject("some bank");
		depositRequest.setSourceAccount("12345");
		
		PaymentDTO theDeposit = new PaymentDTO();
		theDeposit.setAmount(10);
		theDeposit.setSourceSubject("some bank");
		theDeposit.setSourceAccount("12345");
		theDeposit.setDestinationAccount("targetVaultId");
		
		when(paymentService.deposit(any(PaymentDTO.class))).thenReturn(theDeposit);
		
		OperationStatusModel response = paymentController.makeDeposit(depositRequest);
		
		assertNotNull(response);
		assertEquals(RequestOperationName.DEPOSIT.name(), response.getOperationName());
		assertEquals(RequestOperationStatus.SUCCESS.name(), response.getOperationResult());
		verify(paymentService, times(1)).deposit(any(PaymentDTO.class));
	}
	
	@Test
	void testMakeWithdrawal() {
		WithdrawalRequestModel withdrawalRequest = new WithdrawalRequestModel();
		withdrawalRequest.setAmount(10);
		withdrawalRequest.setSourceAccount("sourceVaultId");
		withdrawalRequest.setDestinationSubject("some bank");
		withdrawalRequest.setDestinationAccount("12345");
		
		PaymentDTO theWithdrawal = new PaymentDTO();
		theWithdrawal.setAmount(10);
		theWithdrawal.setSourceAccount("sourceVaultId");
		theWithdrawal.setDestinationSubject("some bank");
		theWithdrawal.setDestinationAccount("12345");
		
		when(paymentService.withdraw(any(PaymentDTO.class))).thenReturn(theWithdrawal);
		
		OperationStatusModel response = paymentController.makeWithdrawal(withdrawalRequest);
		
		assertNotNull(response);
		assertEquals(RequestOperationName.WITHDRAWAL.name(), response.getOperationName());
		assertEquals(RequestOperationStatus.SUCCESS.name(), response.getOperationResult());
		verify(paymentService, times(1)).withdraw(any(PaymentDTO.class));
	}
}
