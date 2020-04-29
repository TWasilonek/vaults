package com.tomaszwasilonek.vaults.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tomaszwasilonek.vaults.ws.entity.Payment;
import com.tomaszwasilonek.vaults.ws.repositories.PaymentRepository;
import com.tomaszwasilonek.vaults.ws.shared.constants.ApplicationConstants;
import com.tomaszwasilonek.vaults.ws.shared.constants.PaymentType;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;

public class PaymentServiceTest {
	
	@InjectMocks
	PaymentServiceImpl paymentService;
	
	@Mock
	PaymentRepository paymentRepository;
	
	@Mock
	UserVaultServiceImpl userVaultService;
	
	final String SOURCE_VAULT_ID = "sourceVaultId";
	final String TARGET_VAULT_ID = "destinationVaultId";
	
	Payment thePayment;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		thePayment = new Payment();
		thePayment.setAmount(10.00);
	}
	
	@Test
	void testMoneyTransfer() {
		thePayment.setSourceAccount(SOURCE_VAULT_ID);
		thePayment.setDestinationAccount(TARGET_VAULT_ID);
		thePayment.setSourceSubject(ApplicationConstants.APP_NAME);
		thePayment.setDestinationSubject(ApplicationConstants.APP_NAME);
	
		PaymentDTO moneyTransferDTO;
		moneyTransferDTO = new PaymentDTO();
		moneyTransferDTO.setAmount(10.00);
		moneyTransferDTO.setSourceAccount(SOURCE_VAULT_ID);
		moneyTransferDTO.setDestinationAccount(TARGET_VAULT_ID);
		
		when(paymentRepository.save(any(Payment.class))).thenReturn(thePayment);
		
		PaymentDTO storedMoneyTransfer = paymentService.moneyTransfer(moneyTransferDTO);
		
		assertEquals(10.00, storedMoneyTransfer.getAmount());
		assertEquals(SOURCE_VAULT_ID, storedMoneyTransfer.getSourceAccount());
		assertEquals(TARGET_VAULT_ID, storedMoneyTransfer.getDestinationAccount());
		assertEquals(ApplicationConstants.APP_NAME, storedMoneyTransfer.getDestinationSubject());
		assertEquals(ApplicationConstants.APP_NAME, storedMoneyTransfer.getSourceSubject());
		
		verify(paymentRepository, times(1)).save(any(Payment.class));
		verify(userVaultService, times(1)).moneyTransfer(any(PaymentDTO.class));
	}

	@Test
	void testDeposit() {
		thePayment.setSourceAccount("12345");
		thePayment.setSourceSubject("some bank");
		thePayment.setDestinationAccount(TARGET_VAULT_ID);
		thePayment.setDestinationSubject(ApplicationConstants.APP_NAME);
		thePayment.setPaymentType(PaymentType.DEPOSIT);
		
		PaymentDTO theDeposit = new PaymentDTO();
		theDeposit.setPaymentType(PaymentType.DEPOSIT);
		theDeposit.setAmount(10);
		theDeposit.setSourceAccount("12345");
		theDeposit.setSourceSubject("some bank");
		theDeposit.setDestinationAccount(TARGET_VAULT_ID);
		
		when(paymentRepository.save(any(Payment.class))).thenReturn(thePayment);
		
		PaymentDTO storedDeposit = paymentService.deposit(theDeposit);
		
		assertEquals(PaymentType.DEPOSIT, storedDeposit.getPaymentType());
		assertEquals(10.00, storedDeposit.getAmount());
		assertEquals(TARGET_VAULT_ID, storedDeposit.getDestinationAccount());
		assertEquals(ApplicationConstants.APP_NAME, storedDeposit.getDestinationSubject());
		assertEquals("12345", storedDeposit.getSourceAccount());
		assertEquals("some bank", storedDeposit.getSourceSubject());
		
		verify(paymentRepository, times(1)).save(any(Payment.class));
		verify(userVaultService, times(1)).deposit(any(PaymentDTO.class));
	}
	
	@Test
	void testWithdrawal() {
		thePayment.setSourceAccount(SOURCE_VAULT_ID);
		thePayment.setSourceSubject(ApplicationConstants.APP_NAME);
		thePayment.setDestinationAccount("12345");
		thePayment.setDestinationSubject("some bank");
		
		PaymentDTO theWithdrawal = new PaymentDTO();
		theWithdrawal.setAmount(10);
		theWithdrawal.setSourceAccount(SOURCE_VAULT_ID);
		theWithdrawal.setSourceSubject(ApplicationConstants.APP_NAME);
		theWithdrawal.setDestinationAccount("12345");
		
		when(paymentRepository.save(any(Payment.class))).thenReturn(thePayment);
		
		PaymentDTO storedWithdrawal = paymentService.withdraw(theWithdrawal);
		
		assertEquals(10.00, storedWithdrawal.getAmount());
		assertEquals(SOURCE_VAULT_ID, storedWithdrawal.getSourceAccount());
		assertEquals(ApplicationConstants.APP_NAME, storedWithdrawal.getSourceSubject());
		assertEquals("12345", storedWithdrawal.getDestinationAccount());
		assertEquals("some bank", storedWithdrawal.getDestinationSubject());
		
		verify(paymentRepository, times(1)).save(any(Payment.class));
		verify(userVaultService, times(1)).withdraw(any(PaymentDTO.class));
	}
}
