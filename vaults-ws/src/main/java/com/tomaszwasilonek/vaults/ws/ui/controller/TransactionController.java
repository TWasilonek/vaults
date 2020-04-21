package com.tomaszwasilonek.vaults.ws.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomaszwasilonek.vaults.ws.service.TransactionService;
import com.tomaszwasilonek.vaults.ws.shared.dto.InternalTransactionDTO;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;

@RestController
@RequestMapping("/api/transactions") // http://localhost:8888/api/transactions
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;

	// TODO: add body validation
	@PostMapping("/internal")
	public OperationStatusModel postInternalTransaction(@RequestBody InternalTransactionDTO transaction) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.INTERNAL_TRANSACTION.name());
		
		transactionService.makeInternalTransaction(transaction);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	// TODO: /external/withdraw
	// TODO: /external/deposit
}
