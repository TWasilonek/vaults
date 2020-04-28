package com.tomaszwasilonek.vaults.ws.service;

import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;

public interface PaymentService {
	PaymentDTO moneyTransfer(PaymentDTO moneyTransfer);
	
	PaymentDTO deposit(PaymentDTO theDeposit);
}
