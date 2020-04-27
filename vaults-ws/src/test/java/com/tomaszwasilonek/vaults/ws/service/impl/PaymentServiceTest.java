package com.tomaszwasilonek.vaults.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tomaszwasilonek.vaults.ws.entity.Payment;
import com.tomaszwasilonek.vaults.ws.entity.UserVault;
import com.tomaszwasilonek.vaults.ws.repositories.PaymentRepository;
import com.tomaszwasilonek.vaults.ws.shared.dto.MoneyTransferDTO;

public class PaymentServiceTest {
	
	@InjectMocks
	PaymentServiceImpl paymentService;
	
	@Mock
	PaymentRepository paymentRepository;
	
	@Mock
	UserVaultServiceImpl userVaultService;
	
	final String SOURCE_VAULT_ID = "sourceVaultId";
	final String TARGET_VAULT_ID = "destinationVaultId";
	
	Payment moneyTransfer;
	MoneyTransferDTO moneyTransferDTO;
	
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		moneyTransfer = new Payment();
		moneyTransfer.setAmount(10.00);
		moneyTransfer.setSourceAccount(SOURCE_VAULT_ID);
		moneyTransfer.setDestinationAccount(TARGET_VAULT_ID);
	
		moneyTransferDTO = new MoneyTransferDTO();
		moneyTransferDTO.setAmount(10.00);
		moneyTransferDTO.setSourceAccount(SOURCE_VAULT_ID);
		moneyTransferDTO.setDestinationAccount(TARGET_VAULT_ID);
	}
	
	@Test
	void testMakeMoneyTransfer() {
		when(paymentRepository.save(any(Payment.class))).thenReturn(moneyTransfer);
		
		MoneyTransferDTO storedTransaction = paymentService.makeMoneyTransfer(moneyTransferDTO);
		
		assertEquals(10.00, storedTransaction.getAmount());
		assertEquals(SOURCE_VAULT_ID, storedTransaction.getSourceAccount());
		assertEquals(TARGET_VAULT_ID, storedTransaction.getDestinationAccount());
		
		verify(paymentRepository, times(1)).save(any(Payment.class));
		verify(userVaultService, times(1)).makeMoneyTransfer(any(MoneyTransferDTO.class));
	}

}
