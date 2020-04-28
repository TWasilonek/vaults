package com.tomaszwasilonek.vaults.ws.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomaszwasilonek.vaults.ws.service.PaymentService;
import com.tomaszwasilonek.vaults.ws.shared.dto.MoneyTransferDTO;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;

@RestController
@RequestMapping("/api/transactions")
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;

	// TODO: add body validation
	// TODO: test if Vaults belong to User -> maybe an Aspect or Annotation?
	@PostMapping("/money-transfer")
	public OperationStatusModel makeMoneyTransfer(@RequestBody MoneyTransferDTO moneyTransfer) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.MONEY_TRANSFER.name());
		
		paymentService.moneyTransfer(moneyTransfer);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	// TODO: /external/deposit
	// TODO: /external/withdraw
}
