package com.tomaszwasilonek.vaults.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomaszwasilonek.vaults.ws.entity.Transaction;
import com.tomaszwasilonek.vaults.ws.repositories.TransactionRepository;
import com.tomaszwasilonek.vaults.ws.service.TransactionService;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.dto.InternalTransactionDTO;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	UserVaultService userVaultService;

	@Override
	public InternalTransactionDTO makeInternalTransaction(InternalTransactionDTO transaction) {
		
		// TODO: VERIFY that vaults exist and are assigned to the same User
		
		// TODO: is it the best way to do event sourcing?
		Transaction transactionEntity = new Transaction();
		BeanUtils.copyProperties(transaction, transactionEntity);
		
		userVaultService.applyInternalTransaction(transaction);
		
		return saveAndReturnInternalTransaction(transactionEntity);
	}
	
	private InternalTransactionDTO saveAndReturnInternalTransaction(Transaction transaction) {
		Transaction storedTransaction = transactionRepository.save(transaction);
		return mapTransactionEntityToInternalTransactionDto(storedTransaction);
	}
	
	private InternalTransactionDTO mapTransactionEntityToInternalTransactionDto(Transaction transaction) {
		InternalTransactionDTO returnValue = new InternalTransactionDTO();
		BeanUtils.copyProperties(transaction, returnValue);
		return returnValue;
	}
}
