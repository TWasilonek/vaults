package com.tomaszwasilonek.vaults.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomaszwasilonek.vaults.ws.entity.Payment;
import com.tomaszwasilonek.vaults.ws.repositories.PaymentRepository;
import com.tomaszwasilonek.vaults.ws.service.PaymentService;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.dto.MoneyTransferDTO;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	PaymentRepository transactionRepository;
	
	@Autowired
	UserVaultService userVaultService;

	@Override
	public MoneyTransferDTO makeMoneyTransfer(MoneyTransferDTO moneyTransfer) {
		
		// TODO: VERIFY that vaults exist and are assigned to the same User
		
		// TODO: is it the best way to do event sourcing?
		Payment paymentEntity = new Payment();
		BeanUtils.copyProperties(moneyTransfer, paymentEntity);
		
		userVaultService.makeMoneyTransfer(moneyTransfer);
		
		return saveAndReturnPayment(paymentEntity);
	}
	
	private MoneyTransferDTO saveAndReturnPayment(Payment payment) {
		Payment storedTransaction = transactionRepository.save(payment);
		return mapPaymentEntityToMoneyTransferDTO(storedTransaction);
	}
	
	private MoneyTransferDTO mapPaymentEntityToMoneyTransferDTO(Payment payment) {
		MoneyTransferDTO returnValue = new MoneyTransferDTO();
		BeanUtils.copyProperties(payment, returnValue);
		return returnValue;
	}
}
