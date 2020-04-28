package com.tomaszwasilonek.vaults.ws.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomaszwasilonek.vaults.ws.service.PaymentService;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;

@RestController
@RequestMapping("/api/transactions")
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;

	// TODO: test if Vaults belong to User -> maybe an Aspect or Annotation?
	@PostMapping("/money-transfer")
	public OperationStatusModel makeMoneyTransfer(@RequestBody PaymentDTO moneyTransfer) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.MONEY_TRANSFER.name());
		
		paymentService.moneyTransfer(moneyTransfer);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@PostMapping("/deposit")
	public OperationStatusModel makeDeposit(@RequestBody PaymentDTO theDeposit) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DEPOSIT.name());
		
		paymentService.deposit(theDeposit);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	// TODO: /external/withdraw
}
