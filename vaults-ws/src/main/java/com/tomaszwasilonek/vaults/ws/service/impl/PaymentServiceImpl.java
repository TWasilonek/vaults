package com.tomaszwasilonek.vaults.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomaszwasilonek.vaults.ws.entity.Payment;
import com.tomaszwasilonek.vaults.ws.repositories.PaymentRepository;
import com.tomaszwasilonek.vaults.ws.service.PaymentService;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.dto.MoneyTransferDTO;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;

@Service
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	PaymentRepository transactionRepository;
	
	@Autowired
	UserVaultService userVaultService;

	@Override
	public MoneyTransferDTO moneyTransfer(MoneyTransferDTO moneyTransfer) {
		
		// TODO: is it the best way to do event sourcing?
		Payment paymentEntity = new Payment();
		BeanUtils.copyProperties(moneyTransfer, paymentEntity);
		
		userVaultService.moneyTransfer(moneyTransfer);
		
		return saveAndReturnPayment(paymentEntity);
	}
	
	@Override
	public PaymentDTO deposit(PaymentDTO theDeposit) {
		// TODO Auto-generated method stub
		return null;
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
