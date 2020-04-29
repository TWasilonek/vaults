package com.tomaszwasilonek.vaults.ws.service.impl;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomaszwasilonek.vaults.ws.entity.Payment;
import com.tomaszwasilonek.vaults.ws.repositories.PaymentRepository;
import com.tomaszwasilonek.vaults.ws.service.PaymentService;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.constants.ApplicationConstants;
import com.tomaszwasilonek.vaults.ws.shared.constants.PaymentType;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	UserVaultService userVaultService;

	@Override
	public PaymentDTO moneyTransfer(PaymentDTO moneyTransfer) {
		Payment paymentEntity = new Payment();
		BeanUtils.copyProperties(moneyTransfer, paymentEntity);
		
		// the destination and source accounts are user Vault
		paymentEntity.setDestinationSubject(ApplicationConstants.APP_NAME);
		paymentEntity.setSourceSubject(ApplicationConstants.APP_NAME);
		paymentEntity.setPaymentType(PaymentType.MONEY_TRANSFER);
		paymentEntity.setPaymentId(UUID.randomUUID().toString());
		
		// TODO: change the API to be more generic, ex. setVaultBalance(vaultId, balance);
		// update the user vaults
		userVaultService.moneyTransfer(moneyTransfer);
		
		return saveAndReturnPayment(paymentEntity);
	}
	
	@Override
	public PaymentDTO deposit(PaymentDTO theDeposit) {
		Payment paymentEntity = new Payment();
		BeanUtils.copyProperties(theDeposit, paymentEntity);
		
		// the destination account is a user Vault
		paymentEntity.setDestinationSubject(ApplicationConstants.APP_NAME);
		paymentEntity.setPaymentType(PaymentType.DEPOSIT);
		paymentEntity.setPaymentId(UUID.randomUUID().toString());
		
		// update the user vault
		userVaultService.deposit(theDeposit);
		
		return saveAndReturnPayment(paymentEntity);
	}
	
	@Override
	public PaymentDTO withdraw(PaymentDTO theWithdrawal) {
		Payment paymentEntity = new Payment();
		BeanUtils.copyProperties(theWithdrawal, paymentEntity);
		
		// the source account is a user vault
		paymentEntity.setSourceSubject(ApplicationConstants.APP_NAME);
		paymentEntity.setPaymentType(PaymentType.WITHDRAWAL);
		paymentEntity.setPaymentId(UUID.randomUUID().toString());
		
		// update the user vault
		userVaultService.withdraw(theWithdrawal);
		
		return saveAndReturnPayment(paymentEntity);
	}
	
	private PaymentDTO saveAndReturnPayment(Payment payment) {
		Payment storedPayment = paymentRepository.save(payment);
		return mapPaymentEntityToPaymentDTO(storedPayment);
	}
	
	private PaymentDTO mapPaymentEntityToPaymentDTO(Payment payment) {
		PaymentDTO returnValue = new PaymentDTO();
		BeanUtils.copyProperties(payment, returnValue);
		return returnValue;
	}
}
