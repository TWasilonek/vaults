package com.tomaszwasilonek.vaults.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomaszwasilonek.vaults.ws.entity.Payment;
import com.tomaszwasilonek.vaults.ws.repositories.PaymentRepository;
import com.tomaszwasilonek.vaults.ws.service.PaymentService;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	PaymentRepository transactionRepository;
	
	@Autowired
	UserVaultService userVaultService;

	@Override
	public PaymentDTO moneyTransfer(PaymentDTO moneyTransfer) {
		Payment paymentEntity = new Payment();
		BeanUtils.copyProperties(moneyTransfer, paymentEntity);
		
		// update the user vaults
		userVaultService.moneyTransfer(moneyTransfer);
		
		return saveAndReturnPayment(paymentEntity);
	}
	
	@Override
	public PaymentDTO deposit(PaymentDTO theDeposit) {
		Payment paymentEntity = new Payment();
		BeanUtils.copyProperties(theDeposit, paymentEntity);
		
		// update the user vault
//		userVaultService.deposit(theDeposit);
		
		return saveAndReturnPayment(paymentEntity);
	}
	
	private PaymentDTO saveAndReturnPayment(Payment payment) {
		Payment storedTransaction = transactionRepository.save(payment);
		return mapPaymentEntityToPaymentDTO(storedTransaction);
	}
	
	private PaymentDTO mapPaymentEntityToPaymentDTO(Payment payment) {
		PaymentDTO returnValue = new PaymentDTO();
		BeanUtils.copyProperties(payment, returnValue);
		return returnValue;
	}

}
