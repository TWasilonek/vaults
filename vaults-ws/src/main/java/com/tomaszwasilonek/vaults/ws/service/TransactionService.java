package com.tomaszwasilonek.vaults.ws.service;

import com.tomaszwasilonek.vaults.ws.shared.dto.InternalTransactionDTO;

public interface TransactionService {
	InternalTransactionDTO makeInternalTransaction(InternalTransactionDTO transaction);

}
