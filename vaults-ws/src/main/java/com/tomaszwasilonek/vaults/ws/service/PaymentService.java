package com.tomaszwasilonek.vaults.ws.service;

import com.tomaszwasilonek.vaults.ws.shared.dto.MoneyTransferDTO;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;

public interface PaymentService {
	MoneyTransferDTO moneyTransfer(MoneyTransferDTO moneyTransfer);
	
	PaymentDTO deposit(PaymentDTO theDeposit);
}
