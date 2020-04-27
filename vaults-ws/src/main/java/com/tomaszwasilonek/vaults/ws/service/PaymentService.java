package com.tomaszwasilonek.vaults.ws.service;

import com.tomaszwasilonek.vaults.ws.shared.dto.MoneyTransferDTO;

public interface PaymentService {
	MoneyTransferDTO makeMoneyTransfer(MoneyTransferDTO moneyTransfer);

}
