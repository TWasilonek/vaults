package com.tomaszwasilonek.vaults.ws.service.impl;

import org.springframework.stereotype.Service;

import com.tomaszwasilonek.vaults.ws.service.TransactionService;
import com.tomaszwasilonek.vaults.ws.shared.dto.InternalTransactionDTO;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Override
	public InternalTransactionDTO makeInternalTransaction(InternalTransactionDTO transaction) {
		return new InternalTransactionDTO();
	}

}
